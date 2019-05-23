package com.alextim;

import org.objectweb.asm.*;

import java.lang.annotation.Annotation;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

//java -javaagent:instrumentationDemo.jar -jar instrumentationDemo.jar
public class Agent {

    private static class Method {
        final String name;
        final String descriptor;
        final String signature;
        final String[] exceptions;

        public Method(String name, String descriptor, String signature, String[] exceptions) {
            this.name = name;
            this.descriptor = descriptor;
            this.signature = signature;
            this.exceptions = exceptions;
        }
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                List<Method> methods = findMethodsByAnnotation(classfileBuffer, Log.class);

                if(!methods.isEmpty())
                    return addProxyMethods(className, classfileBuffer, methods);
                return null;
            }
        });
    }

    private static List<Method> findMethodsByAnnotation(byte[] originalClass, Class<? extends Annotation> desiredAnnotation) {
        ClassReader reader = new ClassReader(originalClass);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
        String desiredDescriptor = Type.getDescriptor(desiredAnnotation);

        List<Method> methods = new ArrayList<>();
        reader.accept(new ClassVisitor(Opcodes.ASM5, writer) {
            @Override
            public MethodVisitor visitMethod(int access, String methodName, String methodDescriptor, String methodSignature, String[] methodExceptions) {
                return new MethodVisitor(api, super.visitMethod(access, methodName, methodDescriptor, methodSignature, methodExceptions)) {
                    @Override
                    public AnnotationVisitor visitAnnotation(String annotationDescriptor, boolean visible) {

                        if(annotationDescriptor.equals(desiredDescriptor)) {
                            methods.add(new Method(methodName, methodDescriptor, methodSignature, methodExceptions));
                        }
                        return super.visitAnnotation(annotationDescriptor, visible);
                    }
                };
            }
        }, Opcodes.ASM5);
        return methods;
    }

    private static byte[] addProxyMethods(String className, byte[] originalClass,  List<Method> methods) {
        for(int i =0; i<methods.size(); i++) {
            ClassReader reader = new ClassReader(originalClass);
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);

            int finalI = i;
            reader.accept(new ClassVisitor(Opcodes.ASM5, writer) {
                @Override
                public MethodVisitor visitMethod(int access, String methodName, String methodDescriptor, String signature, String[] exceptions) {
                    if(methodName.equals(methods.get(finalI).name)) {
                        return super.visitMethod(access, methodName + "Proxied", methodDescriptor, signature, exceptions);
                    }
                    else {
                        return super.visitMethod(access, methodName, methodDescriptor, signature, exceptions);
                    }
                }
            }, Opcodes.ASM5);

            MethodVisitor mv = writer.visitMethod(Opcodes.ACC_PUBLIC, methods.get(i).name, methods.get(i).descriptor, methods.get(i).signature, methods.get(i).exceptions);

            //java.io.PrintStream stream = System.out;
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

            //StringBuilder builder = new StringBuilder();
            mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(java.lang.StringBuilder.class));
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);

            //builder.append("Execute " + methods.get(i).name + " with param: ");
            mv.visitLdcInsn("Executed " + methods.get(i).name + " with param: ");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);


            Type[] argumentTypes = Type.getArgumentTypes(methods.get(i).descriptor);
            for(int j=0; j<argumentTypes.length; j++) {
                //builder.append(var1);
                mv.visitVarInsn(argumentTypes[j].getOpcode(Opcodes.ILOAD), j + 1);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(" + argumentTypes[j].getDescriptor() + ")Ljava/lang/StringBuilder;", false);
                //builder.append("." / ",");
                mv.visitLdcInsn(j == argumentTypes.length -1 ? "." : ", ");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            }

            //String str = builder.toString();
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);

            //stream.println(str);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            //this.funProxied(var1);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            for(int j=0; j<argumentTypes.length; j++)
                mv.visitVarInsn(argumentTypes[j].getOpcode(Opcodes.ILOAD), j + 1);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, methods.get(i).name + "Proxied", methods.get(i).descriptor, false);

            mv.visitInsn(Type.getReturnType(methods.get(i).descriptor).getOpcode(Opcodes.IRETURN));
            mv.visitMaxs(0, 0);
            mv.visitEnd();

            originalClass = writer.toByteArray();
        }

        return originalClass;
    }

}
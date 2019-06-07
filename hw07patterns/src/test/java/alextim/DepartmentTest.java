package alextim;

import com.alextim.atm.Atm;
import com.alextim.department.Department;
import com.alextim.department.SimpleDepartment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static com.alextim.banknotes.RubBanknote.*;

public class DepartmentTest {

    private Department department;

    @BeforeEach
    void setUp() {
        department = new SimpleDepartment();
    }

    public static void createInitStateForSpbAtm(Atm atm) {
        atm.putDeposit(Rub5000, 1);
        atm.putDeposit(Rub2000, 2);
        atm.putDeposit(Rub1000, 1);
        atm.putDeposit(Rub500, 2);
        atm.putDeposit(Rub200, 1);
        atm.putDeposit(Rub100, 1); //11300
    }

    public static void createInitStateForSberAtm(Atm atm) {
        atm.putDeposit(Rub5000, 2);
        atm.putDeposit(Rub2000, 1);
        atm.putDeposit(Rub1000, 2);
        atm.putDeposit(Rub500, 1);
        atm.putDeposit(Rub200, 2);
        atm.putDeposit(Rub100, 1); //15000
    }

    @Test
    public void createAtmTest() {
        Atm atm1 = department.createAtm("Spb", DepartmentTest::createInitStateForSpbAtm);
        Atm atm2 = department.createAtm("Sber", DepartmentTest::createInitStateForSberAtm);
        Assertions.assertTrue(atm1 != atm2);
    }

    @Test
    public void resetEventTest() {
        Atm atm1 = department.createAtm("Spb", DepartmentTest::createInitStateForSpbAtm);
        Atm atm2 = department.createAtm("Sber", DepartmentTest::createInitStateForSberAtm);

        atm1.putDeposit(Rub500, 2);
        atm1.putDeposit(Rub2000, 3);

        atm2.putDeposit(Rub100, 4);
        atm2.putDeposit(Rub2000, 3);

        department.resetEvent();

        Assertions.assertEquals(26300, department.getTotalBalance());
    }

    @Test
    public void getBalanceTest() {
        Atm atm1 = department.createAtm("Spb", DepartmentTest::createInitStateForSpbAtm);
        Atm atm2 = department.createAtm("Sber", DepartmentTest::createInitStateForSberAtm);
        Atm atm3 = department.createAtm("Sber", DepartmentTest::createInitStateForSberAtm);

        atm1.putDeposit(Rub2000, 2);
        atm2.putDeposit(Rub1000, 1);
        atm3.putDeposit(Rub500, 1);

        Assertions.assertEquals(46800, ((SimpleDepartment)department).getTotalBalance2());
    }

}

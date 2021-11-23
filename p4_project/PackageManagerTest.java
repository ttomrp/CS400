import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PackageManagerTest {
	PackageManager pkgManager;

	@BeforeEach
	void setUp() throws Exception {
		pkgManager = new PackageManager();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		pkgManager = null;
	}

	@Test
	void test_constructGraph() {
		try {
			pkgManager.constructGraph("valid.json");
			if (pkgManager.getAllPackages().isEmpty())
				fail("package manager didn't construct graph");
		} catch (Exception e) {
			e.printStackTrace();
			fail("unexcpected exception thrown");
		}
	}

	@Test
	void test_toInstall_valid() {
		try {
			pkgManager.constructGraph("valid.json");
			List<String> correctList = new LinkedList<String>();
			correctList.add("D");
			correctList.add("B");
			correctList.add("A");
			List<String> testList = pkgManager.toInstall("A", "C");
			if (!testList.equals(correctList)) {
				fail("testList does not equal correctList");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("unexpected exception thrown");
		}
	}

	@Test
	void test_listAllPackages_valid() throws CycleException {
		try {
			pkgManager.constructGraph("valid.json");
			String packages = pkgManager.getAllPackages().toString();
			if (!packages.equals("[A, B, C, D, E]"))
				fail("does not find correct packages");
		} catch (Exception e) {
			e.printStackTrace();
			fail("unexcpected exception thrown");
		}
	}

	@Test
	void test_getPackageWithMaxDependencies_valid() {
		try {
			pkgManager.constructGraph("valid.json");
			String pkgMaxDeps = pkgManager.getPackageWithMaxDependencies();
			if (!pkgMaxDeps.equals("A"))
				fail("failed to get package with the max dependencies");
		} catch (Exception e) {
			e.printStackTrace();
			fail("unexcpected exception thrown");
		}
	}

	@Test
	void test_getInstallationOrder_valid() throws FileNotFoundException, IOException, ParseException {
		try {
			pkgManager.constructGraph("valid.json");
			List<String> correctList = new LinkedList<String>();
			correctList.add("D");
			correctList.add("C");
			correctList.add("B");
			correctList.add("A");
			List<String> installOrder = pkgManager.getInstallationOrder("A");
			if (!installOrder.equals(correctList)) {
				fail("install order is wrong");
			}
		} catch (CycleException e) {
			fail("CycleException thrown");
		} catch (Exception e) {
			e.printStackTrace();
			fail("unexcpected exception thrown");
		}
	}

	@Test
	void test_getInstallationOrderForAllPackages_valid() throws FileNotFoundException, IOException, ParseException {
		try {
			pkgManager.constructGraph("valid.json");
			List<String> correctList = new LinkedList<String>();
			correctList.add("D");
			correctList.add("C");
			correctList.add("B");
			correctList.add("A");
			correctList.add("E");
			List<String> installOrder = pkgManager.getInstallationOrderForAllPackages();
			if (!installOrder.equals(correctList)) {
				fail("install order is wrong");
			}
		} catch (CycleException e) {
			e.printStackTrace();
			fail("CycleException thrown");
		} catch (Exception e) {
			e.printStackTrace();
			fail("unexcpected exception thrown");
		}
	}

	@Test
	void test_FileNotFoundException() {
		try {
			pkgManager.constructGraph("badfile.json");
			fail("FileNotFoundException was not thrown");
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("unexcpected exception thrown");
		}
	}

	@Test
	void test_getInstallationOrderForAllPackages_cycle_detection()
			throws FileNotFoundException, IOException, ParseException, PackageNotFoundException {
		try {
			pkgManager.constructGraph("shared_dependencies.json");
			pkgManager.getInstallationOrderForAllPackages();
			fail("CycleException not thrown");
		} catch (CycleException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("unexcpected exception thrown");
		}
	}

	@Test
	void test_getInstallationOrder_cycle_detection() {
		try {
			pkgManager.constructGraph("shared_dependencies.json");
			pkgManager.getInstallationOrder("A");
			fail("Did not throw CycleException");
		} catch (CycleException e) {
		} catch (PackageNotFoundException e) {
			e.printStackTrace();
			fail("PackageNotFoundException thrown");
		} catch (Exception e) {
			e.printStackTrace();
			fail("unexcpected exception thrown");
		}
	}
}

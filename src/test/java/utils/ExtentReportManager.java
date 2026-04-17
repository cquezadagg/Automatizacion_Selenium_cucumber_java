package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("results/TestReport.html");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Project", "Automatizacion de pruebas con java selenium ");
        }
        return extent;
    }
}

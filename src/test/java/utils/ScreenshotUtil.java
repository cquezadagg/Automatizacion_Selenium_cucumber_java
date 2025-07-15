package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {

    public static String takeScreenshot(WebDriver driver, String fileName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);

        String screenshotsDir = "results/screenshots";
        String destPath = screenshotsDir + "/" + fileName + ".png";

        try {
            Files.createDirectories(Path.of(screenshotsDir));
            Files.copy(src.toPath(), Path.of(destPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destPath; // Debe ser ruta relativa al reporte
    }
}

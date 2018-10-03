package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


public class VuForiaTest extends LinearOpMode {

    public static final String TAG = "";
    OpenGLMatrix lastLocation = null;

    VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() throws InterruptedException{

        int cameraMonitorView = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorView);

        parameters.vuforiaLicenseKey = "AWG3P/z/////AAABmSMMaCWVgkfmo2UovOCtI/hOG/YY8Z7FMPwI/GMArP6MmHNdVJce5HKL4HqEReuMPpmegV5nA9TtQPpWWgmn51rIG2jwxeUgjr6Drs4mh4hAKoqD5BQO63gPBcYqBm84SHlS2lsAt37ykhyW+6TGByVWtlnSMcupISrvih/8Mw3v/wCJbKgx+JFIK7XCF23A4Ev5k2wM2QIXBTloJBzYX8jwuPHNy7E5M5IJs9n2zmZbmSfvQtfXJK3KlPxRamB9OharPClWixrSmXz5eVrs266w27icP91UqwghkU90JObmReGgpVTksqc9ineaPQwx9CmMVASqx6hfprJWQisp5Y/b8nUZ8fxguVrmbua+Z1yF";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables goldBlock = this.vuforia.loadTrackablesFromAsset(""); //TODO: Fill this in with the scanned object file.

        telemetry.addData(">","Press play to start.");
        telemetry.update();

        while (opModeIsActive()){


        }


    }
}

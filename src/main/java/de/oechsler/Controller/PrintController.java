package de.oechsler.Controller;

import javafx.print.PrinterJob;
import javafx.scene.transform.Scale;

public class PrintController {

    private ReferenceHandler referenceHandler;

    public PrintController(ReferenceHandler referenceHandler) {
        this.referenceHandler = referenceHandler;
        referenceHandler.getAutomatonStage().getPrintMenuItem().setOnAction(e -> print());
        referenceHandler.getAutomatonStage().getPrintPopulationButton().setOnAction(e -> print());
    }
    public void print() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(this.referenceHandler.getAutomatonStage().getPopulationPane()
                .getCanvas().getScene().getWindow())) {

            double scale = Math.min(job.getJobSettings().getPageLayout().getPrintableWidth() / this.referenceHandler.getAutomatonStage()
                            .getPopulationPane().getCanvas().getWidth(),
                    job.getJobSettings().getPageLayout().getPrintableHeight() / this.referenceHandler.getAutomatonStage().getPopulationPane()
                            .getCanvas().getHeight());
            this.referenceHandler.getAutomatonStage().getPopulationPane().getCanvas().getTransforms().add(new Scale(scale, scale));
            boolean success = job.printPage(this.referenceHandler.getAutomatonStage().getPopulationPane().getCanvas());
            this.referenceHandler.getAutomatonStage().getPopulationPane().getCanvas().getTransforms()
                    .remove(this.referenceHandler.getAutomatonStage().getPopulationPane().getCanvas().getTransforms().size() - 1);

            if (success) {
                job.endJob();
            }
        }
    }

}


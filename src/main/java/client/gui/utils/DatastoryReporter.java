package client.gui.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DatastoryReporter {
	
	private StringBuilder htmlContent;

	
	
	public DatastoryReporter() {
        htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html>\n");
        htmlContent.append("<html>\n");
        htmlContent.append("<head>\n");
        htmlContent.append("<title>Chart Queries Report</title>\n");
        htmlContent.append("<style>\n");
        htmlContent.append(".container { display: flex; align-items: center; }\n");
        htmlContent.append(".container img { width: 700px; height: 500px; margin-right: 30px; }\n");
//        htmlContent.append(".container img { flex: 2; width: 100%; height: auto; margin-right: 10px; }\n");
//        htmlContent.append(".container p { flex: 1; }\n");
        htmlContent.append(".container div { max-width: 100%; }\n");
        htmlContent.append("h1 { text-align: center; }\n");
        htmlContent.append("h2 { text-align: center; } \n");
        htmlContent.append("</style>\n");
        htmlContent.append("</head>\n");
        htmlContent.append("<body>\n");
	}
	
	public void addTitle(String title) {
	    htmlContent.append("<h1 class=\"h1\">").append(title).append("</h1>\n");
	}

	public void addH2(String text) {
	    htmlContent.append("<h2 class=\"h2\">").append(text).append("</h2>\n");
	}


    public void addText(String text) {
        htmlContent.append("<p>").append(text).append("</p>\n");
    }

    public void addImage(String imagePath, String altText) {
        htmlContent.append("<img src=\"").append(imagePath).append("\" alt=\"").append(altText).append("\">\n");
    }
    
    public void addImageWithText(String imagePath, String altText, String text) {
        htmlContent.append("<div class=\"container\">\n");
        htmlContent.append("<img src=\"").append(imagePath).append("\" alt=\"").append(altText).append("\">\n");
        htmlContent.append("<p>").append(text).append("</p>\n");
        htmlContent.append("</div><br>\n");
    }

    public void finalizeReport() {
        htmlContent.append("</body>\n");
        htmlContent.append("</html>");
    }

    public void saveReport(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(htmlContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

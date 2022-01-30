package com.gradle.webclient;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class WebClientClass {

    public static void main(String[] args) {

        WebClient webClient = new WebClient(BrowserVersion.CHROME);

        HtmlPage page;

        {
            try {

                webClient.getOptions().setCssEnabled(false);
                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
                webClient.getOptions().setThrowExceptionOnScriptError(false);
                webClient.getOptions().setPrintContentOnFailingStatusCode(false);

                page = webClient.getPage("https://www.w3schools.com/");

                webClient.getCurrentWindow().getJobManager().removeAllJobs();
                webClient.close();

                getTitle(page);
                extractLinks(page);
                xPath(page);

            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
            }
        }
    }

    private static void xPath(HtmlPage page) throws IOException {
        List<?> anchors = page.getByXPath("//a[@target='_top']");

        FileWriter lessonsFile = new FileWriter("lessons.csv", true);
        lessonsFile.write("id,name,link\n");

        for (int i = 0; i < anchors.size(); i++) {
            HtmlAnchor link = (HtmlAnchor) anchors.get(i);
            String lessonTitle = link.getTextContent();
            String lessonLink = link.getHrefAttribute();
            lessonsFile.write(i + "," + lessonTitle + "," + lessonLink + "\n");
        }
        lessonsFile.close();
    }

    private static void extractLinks(HtmlPage page) {

        List<HtmlAnchor> links = page.getAnchors();
        for (HtmlAnchor link : links) {
            String href = link.getHrefAttribute();
            System.out.println("Link: " + href);
        }
    }

    private static void getTitle(HtmlPage page) {
        String title = page.getTitleText();
        System.out.println("Page Title: " + title);
    }
}

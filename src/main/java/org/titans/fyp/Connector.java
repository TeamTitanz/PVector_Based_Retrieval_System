package org.titans.fyp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Buddhi on 11/1/2017.
 */
public class Connector {

    int outputCount = 10;
    String pythonInterpreter = "C:\\Users\\Buddhi\\Anaconda2\\envs\\mlPaper\\python.exe";
    String pythonFile = "D:/Project/fyp/word2vec/code/work12/finalSystem/SystemConnector/get_similar_cases.py";
    private PVector pv;

    public Connector() {
        pv = PVector.getInstance();
    }

    public String[] getCaseData(String line) {
        String[] caseData = new String[6];
        String court = "", caseName = "", date = "", caseID = "", arguedDate = "", decidedDate = "";

        if (line.contains("United States Supreme Court")) {
            court = "United States Supreme Court";
            if (line.contains("No.")) {
                String[] tem = line.split("Court")[1].split("No.");
                if (tem[0].contains(",")) {
                    String[] nt = tem[0].split(",");
                    caseName = nt[0].trim();
                    if (nt[1].contains("(") && nt[1].contains(")")) {
                        date = (nt[1].split("\\(")[1].split("\\)")[0]).trim();
                    }
                } else {
                    caseName = tem[0].trim();
                }
                if (tem[1].contains("Argued:")) {
                    caseID = ("No." + tem[1].split("Argued:")[0]).trim();
                    if (tem[1].contains("Decided:")) {
                        String[] td = tem[1].split("Argued:")[1].split("Decided:");
                        arguedDate = td[0].trim();
                        decidedDate = td[1].trim();
                    }
                }
            }
        }

        caseData[0] = court;
        caseData[1] = caseName;
        caseData[2] = date;
        caseData[3] = caseID;
        caseData[4] = arguedDate;
        caseData[5] = decidedDate;
        return caseData;
    }

    private List<String[]> findSimilarCases(String sentences) throws Exception {
        long startTime = System.currentTimeMillis();
        pv.setPVector(sentences);

        List<String[]> similarCasesData = new ArrayList<>();
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", pythonInterpreter + " " + pythonFile + " --count " + outputCount);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            if (line.contains("data=")) {
                BufferedReader br;
                String[] docID = line.substring(5).split(", ");

                try {
                    for (String fileName : docID) {
                        String file = "D:\\Project\\fyp\\word2vec\\code\\work12\\finalSystem\\SystemConnector\\RawCases"
                                + File.separator + fileName + ".txt";
//                        System.out.println(fileName);
                        br = new BufferedReader(new FileReader(file));
                        similarCasesData.add(getCaseData(br.readLine().replaceAll("\\P{Print}", "")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Time Taken: " + (totalTime / 1000.0) + "s");

        return similarCasesData;
    }

    public static void main(String gargs[]) {

//        String par = "I am a divorced wife of a retired veteran. " +
//                "He was ordered to pay me a compensation from his total retirement pay at the divorce. " +
//                "He recently had stopped receiving his retirement pay and now receiving non taxable disability benefits." +
//                " As it was ordered to pay the compensation from his retirement pay, and now he is not receiving it," +
//                " he now refuses to pay my portion.  What should I do?";

        String par = "I am gay. Me and my partner are married for 3 years now. " +
                "I gave birth to a baby with the help of a sperm donor. " +
                "However, birth registration officials refuse to issue the birth certificate " +
                "with the partner’s name as one of the parents, stating that it is legally prohibited to issue.";

        Connector con = new Connector();
        try {
            for (String[] dt : con.findSimilarCases(par)) {
                System.out.println("Court:" + dt[0]);
                System.out.println("Case Name:" + dt[1]);
                System.out.println("Date:" + dt[2]);
                System.out.println("Case ID:" + dt[3]);
                System.out.println("Argued Date:" + dt[4]);
                System.out.println("Decided Date:" + dt[5]);
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

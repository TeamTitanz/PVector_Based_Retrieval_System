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

    private String folderPath = System.getProperty("user.dir");
    private String pythonInterpreter = "C:\\Users\\Buddhi\\Anaconda2\\envs\\mlPaper\\python.exe";
    private String pythonFile = folderPath + File.separator + "SimilarCasesPython.py";
    private PVector pv;

    public Connector() {
        pv = PVector.getInstance();
    }

    private List<Case> findSimilarCases(String sentences, int outputCount) throws Exception {
        long startTime = System.currentTimeMillis();
        pv.setPVector(sentences);

        List<Case> similarCases = new ArrayList<>();
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
                        String file = folderPath + File.separator + "RawCases" + File.separator + fileName + ".txt";
                        br = new BufferedReader(new FileReader(file));
                        Case cs = new Case(fileName);
                        cs.setCaseData(br.readLine().replaceAll("\\P{Print}", ""));
//                        System.out.println(fileName);
                        similarCases.add(cs);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Time Taken: " + (totalTime / 1000.0) + "s");

        return similarCases;
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
            for (Case cs : con.findSimilarCases(par, 10)) {
                System.out.println("ID:" + cs.getId());
                System.out.println("Court:" + cs.getCourt());
                System.out.println("Case Name:" + cs.getCaseName());
                System.out.println("Date:" + cs.getDate());
                System.out.println("Case ID:" + cs.getCaseID());
                System.out.println("Argued Date:" + cs.getArguedDate());
                System.out.println("Decided Date:" + cs.getDecidedDate());
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

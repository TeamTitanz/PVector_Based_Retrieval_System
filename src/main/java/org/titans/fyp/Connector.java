package org.titans.fyp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

/**
 * Created by Buddhi on 11/1/2017.
 */
public class Connector {

    int outputCount = 10;
    String pythonInterpreter = "C:\\Users\\Buddhi\\Anaconda2\\envs\\mlPaper\\python.exe";
    String pythonFile = "D:/Project/fyp/word2vec/code/work12/finalSystem/SystemConnector/get_similar_cases.py";
    private static PVector pv;

    public Connector() {
        pv = new PVector();
    }

    private void findSimilarCases(String sentences) throws Exception {
        long startTime = System.currentTimeMillis();
        pv.setPVector(sentences);

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
//                System.out.println(line.substring(5));
                BufferedReader br;
                String[] docID = line.substring(5).split(", ");

                try {
                    for (String fileName : docID) {
                        String file = "D:\\Project\\fyp\\word2vec\\code\\work12\\finalSystem\\SystemConnector\\RawCases"
                                + File.separator + fileName + ".txt";
                        System.out.println(fileName);
                        br = new BufferedReader(new FileReader(file));
                        System.out.println(br.readLine());
                        System.out.println("\n");
                    }
                } catch (Exception e) {

                }
            }
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Time Taken: " + (totalTime / 1000.0) + "s");
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

        Connector wo = new Connector();
        try {
            wo.findSimilarCases(par);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

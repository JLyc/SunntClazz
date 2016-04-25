//package file_analyzer;
//
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
///**
// * Created by JLyc on 4/23/2016.
// */
//public class Example {
//    public static void main(String[] args) {
//        Path fs = Paths.get("C:\\Java\\Projects\\SunntClazz\\AwesomeExtractor\\resources\\test\\test_group");
//        try(ExtractEarFiles extractEarFiles = new ExtractEarFiles(fs))
//        {
//            for(Path path : extractEarFiles.getZipFiles()) {
//                System.out.println("processes");
//                for (Path sub : extractEarFiles.getProcesses(path)){
//                    System.out.println(sub);
//                }
//                System.out.println("jdbcs");
//                for (Path sub : extractEarFiles.getJDBCs(path)){
//                    System.out.println(sub);
//                }
//            }
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//    }
//}

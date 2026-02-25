// package utils;

// import org.apache.pdfbox.pdmodel.font.PDFont;
// import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

// public class FontUtil{
//     public static PDFont get(Standard14Fonts.FontName fontName){
//         try{
//             Standard14Fonts fonts = new Standard14Fonts(fontName);
//             return  fonts.getFont();
//         } catch (Exception e){
//             System.err.println("Falied to load font: "+fontName);
//             e.printStackTrace();
//             return null;
//         }
//     }
// }
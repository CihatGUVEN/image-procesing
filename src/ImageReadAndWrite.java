import lombok.Data;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Data
public class ImageReadAndWrite extends JPanel{

    private BufferedImage resim;
    private int x1, y1, x2, y2, x3, y3, x4, y4;

    public ImageReadAndWrite(BufferedImage resim, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        this.resim = kesilmisBolgeyiOlustur(resim, x1, y1, x2, y2, x3, y3, x4, y4);
    }

    private BufferedImage kesilmisBolgeyiOlustur(BufferedImage orjinalResim, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        // Kesilecek bölgenin sınırlarını belirle
        int minX = Math.min(Math.min(x1, x2), Math.min(x3, x4));
        int minY = Math.min(Math.min(y1, y2), Math.min(y3, y4));
        int maxX = Math.max(Math.max(x1, x2), Math.max(x3, x4));
        int maxY = Math.max(Math.max(y1, y2), Math.max(y3, y4));

        // Yeni resmi oluştur
        int kesilenGenislik = maxX - minX;
        int kesilenYukseklik = maxY - minY;
        BufferedImage kesilmisResim = new BufferedImage(kesilenGenislik, kesilenYukseklik, BufferedImage.TYPE_INT_RGB);

        // Orjinal resmin ilgili bölgesini kesilen resme kopyala
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                int rgb = orjinalResim.getRGB(x, y);
                kesilmisResim.setRGB(x - minX, y - minY, rgb);
            }
        }

        return kesilmisResim;
    }


    public static void main(String[] args) {
        // Resmin dosya yolu
        String resimDosyaYolu = "karton.jpg";
        List<Point> points = new ArrayList<>();

        try {
            // Resmi yükle
            BufferedImage resim = ImageIO.read(new File(resimDosyaYolu));

            // Bakır renginin RGB değerleri (örnek olarak)
            int bakirR = 184;
            int bakirG = 115;
            int bakirB = 51;

            // Resmin genişliği ve yüksekliği
            int genislik = resim.getWidth();
            int yukseklik = resim.getHeight();

            // Resmin her pikselini kontrol et
            for (int x = 0; x < genislik; x++) {
                for (int y = 0; y < yukseklik; y++) {
                    // Pikselin rengini al
                    int pikselRenk = resim.getRGB(x, y);

                    // RGB değerlerini çıkar
                    int pikselR = (pikselRenk >> 16) & 0xFF;
                    int pikselG = (pikselRenk >> 8) & 0xFF;
                    int pikselB = pikselRenk & 0xFF;

                    // Renkleri karşılaştır
                    if (renkKarsilastir(bakirR, bakirG, bakirB, pikselR, pikselG, pikselB)) {
/*                        System.out.println("Bakır rengi bulundu: x = " + x + ", y = " + y +
                                "  pixelR : " + bakirR +
                                "  pixelG : " + bakirG +
                                "  pixelB : " + bakirB);*/
                        // İlgili işlemleri yapabilirsiniz
                        Point point = new Point();
                        point.setX(x);
                        point.setY(y);
                        point.setR(pikselR);
                        point.setB(pikselB);
                        point.setG(pikselG);
                        points.add(point);

                    }
                }
            }
            if(!points.isEmpty()){
/*                Point topLeft = findTopLeftCorner(points);
                Point topRight = findTopRightCorner(points);
                Point bottomLeft = findBottomLeftCorner(points);
                Point bottomRight = findBottomRightCorner(points);*/

                int width = resim.getWidth();
                int height = resim.getHeight();
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = image.createGraphics();

                // Set background color (you can choose your own background color)
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, width, height);

// Set pixel colors based on Point RGB values
                for (Point point : points) {
                    if (point.getX() >= 0 && point.getX() < width && point.getY() >= 0 && point.getY() < height) {
                        int pixelColor = new Color(point.getR(), point.getG(), point.getB()).getRGB();
                        image.setRGB(point.getX(), point.getY(), pixelColor);
                    } else {
                        System.out.println("Point coordinates out of image bounds: " + point);
                    }
                }
                // Dispose of the graphics object
                g2d.dispose();

                // Save the image to a file
                try {
                    File output = new File("output_image.png");
                    ImageIO.write(image, "png", output);
                    System.out.println("Image saved successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


/*                JFrame frame = new JFrame("Koordinat Aralığını Göster");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(resim.getWidth(), resim.getHeight());
                frame.setContentPane(new ImageReadAndWrite(resim, topLeft.getX(), topLeft.getY(), topRight.getX(), topRight.getY(), bottomLeft.getX(), bottomLeft.getY(), bottomRight.getX(), bottomRight.getY()));
                frame.setVisible(true);

                return;

            }
            System.out.println("Cn not found!!!");*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Kesilmiş bölgeyi çiz
        g.drawImage(resim, 0, 0, this);
    }

    // Renk karşılaştırma fonksiyonu
    public static boolean renkKarsilastir(int r1, int g1, int b1, int r2, int g2, int b2) {
        // RGB değerlerini karşılaştır
        return (r2 - 25 <= r1 && r1 <= r2 + 25) && (g2 - 25 <= g1 && g1 <= g2 + 25) && (b2 - 25 <= b1 || b1 <= b2 + 25);
    }

    private static Point findTopLeftCorner(List<Point> points) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        for (Point point : points) {
            minX = Math.min(minX, point.getX());
            minY = Math.min(minY, point.getY());
        }

        return new Point(minX, minY);
    }

    private static Point findTopRightCorner(List<Point> points) {
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;

        for (Point point : points) {
            maxX = Math.max(maxX, point.getX());
            minY = Math.min(minY, point.getY());
        }

        return new Point(maxX, minY);
    }

    private static Point findBottomLeftCorner(List<Point> points) {
        int minX = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point point : points) {
            minX = Math.min(minX, point.getX());
            maxY = Math.max(maxY, point.getY());
        }

        return new Point(minX, maxY);
    }

    private static Point findBottomRightCorner(List<Point> points) {
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point point : points) {
            maxX = Math.max(maxX, point.getX());
            maxY = Math.max(maxY, point.getY());
        }

        return new Point(maxX, maxY);
    }
}

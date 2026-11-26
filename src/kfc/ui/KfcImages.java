package kfc.ui;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.text.Normalizer;
import java.util.*;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;

/** Dùng chung ảnh cho form quản lý, thực đơn và banner; không phụ thuộc Internet. */
public final class KfcImages {
    private KfcImages() {}
    private static final Map<String, ImageIcon> CACHE = new LinkedHashMap<String, ImageIcon>(64, .75f, true) {
        @Override protected boolean removeEldestEntry(Map.Entry<String, ImageIcon> e) { return size() > 100; }
    };
    public static Path projectRoot() {
        String custom = System.getProperty("kfc.config");
        if (custom != null) return Paths.get(custom).toAbsolutePath().normalize().getParent();
        Path cwd = Paths.get("").toAbsolutePath().normalize();
        if (Files.isRegularFile(cwd.resolve("config.properties"))) return cwd;
        try {
            Path p = Paths.get(KfcImages.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (!Files.isDirectory(p)) p = p.getParent();
            for (int i=0; p!=null && i<5; i++, p=p.getParent())
                if (Files.isRegularFile(p.resolve("config.properties")) || Files.isRegularFile(p.resolve("pom.xml"))) return p;
        } catch (Exception ignored) {}
        return cwd;
    }
    private static Path resolve(String value) {
        Path p=Paths.get(value.replace('\\', '/'));
        if (p.isAbsolute()) return p;
        Path root=projectRoot().resolve(p).normalize();
        if (Files.isRegularFile(root)) return root;
        return p.toAbsolutePath().normalize();
    }
    private static BufferedImage read(Path p) throws IOException {
        if (!Files.isRegularFile(p) || Files.size(p)>10_000_000) return null;
        try(ImageInputStream stream=ImageIO.createImageInputStream(p.toFile())) {
            if(stream==null)return null;
            Iterator<ImageReader> readers=ImageIO.getImageReaders(stream);
            if(!readers.hasNext())return null;
            ImageReader reader=readers.next();
            try { reader.setInput(stream,true,true);int w=reader.getWidth(0),h=reader.getHeight(0);
                if(w<=0||h<=0||w>8000||h>8000||(long)w*h>32_000_000L)throw new IOException("Kích thước ảnh quá lớn");
                javax.imageio.ImageReadParam param=reader.getDefaultReadParam();int step=Math.max(1,Math.max(w,h)/1200);param.setSourceSubsampling(step,step,0,0);
                return reader.read(0,param);
            } finally {reader.dispose();}
        }
    }
    public static String save(File file) throws IOException {
        BufferedImage im=read(file.toPath());if(im==null)throw new IOException("Ảnh không hợp lệ hoặc vượt 10 MB");
        int w=im.getWidth(),h=im.getHeight();double scale=Math.min(1.0,1200.0/Math.max(w,h));
        BufferedImage out=new BufferedImage(Math.max(1,(int)Math.round(w*scale)),Math.max(1,(int)Math.round(h*scale)),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=out.createGraphics();quality(g);g.drawImage(im,0,0,out.getWidth(),out.getHeight(),null);g.dispose();
        String relative="data/images/"+UUID.randomUUID()+".png";Path dest=projectRoot().resolve(relative);
        Files.createDirectories(dest.getParent());if(!ImageIO.write(out,"png",dest.toFile()))throw new IOException("Không lưu được ảnh");return relative;
    }
    public static void removeNew(String relative){try{if(relative.matches("data/images/[0-9a-f-]+\\.png"))Files.deleteIfExists(projectRoot().resolve(relative));}catch(IOException ignored){}}
    public static BufferedImage asset(String path) {
        String name=path.startsWith("/")?path.substring(1):path;
        try {
            for(Path candidate:new Path[]{projectRoot().resolve("src").resolve(name),projectRoot().resolve(name),projectRoot().resolve("images").resolve(Paths.get(name).getFileName())}){
                BufferedImage im=read(candidate);if(im!=null)return im;
            }
            URL url=KfcImages.class.getResource("/"+name);if(url!=null)return ImageIO.read(url);
        }catch(Exception ignored){}return null;
    }
    public static void drawContain(Graphics2D g,Image img,int x,int y,int w,int h){
        int iw=img.getWidth(null),ih=img.getHeight(null);if(iw<=0||ih<=0||w<=0||h<=0)return;
        double scale=Math.min((double)w/iw,(double)h/ih);int nw=Math.max(1,(int)Math.round(iw*scale)),nh=Math.max(1,(int)Math.round(ih*scale));
        g.drawImage(img,x+(w-nw)/2,y+(h-nh)/2,nw,nh,null);
    }
    private static void quality(Graphics2D g){g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);}
    private static String slug(String text){return Normalizer.normalize(text==null?"":text.toLowerCase(Locale.ROOT).replace('đ','d'),Normalizer.Form.NFD).replaceAll("\\p{M}+","").replaceAll("[^a-z0-9]+","_").replaceAll("^_+|_+$","");}
    public static synchronized ImageIcon menuIcon(String code,String name,String path,int w,int h){
        String custom=path==null?"":path.trim();String key=code+"|"+name+"|"+custom+"|"+w+"x"+h;
        if(!custom.isEmpty())try{key+="|"+Files.getLastModifiedTime(resolve(custom)).toMillis();}catch(Exception ignored){}
        ImageIcon cached=CACHE.get(key);if(cached!=null)return cached;
        BufferedImage source=null;
        if(!custom.isEmpty())try{source=read(resolve(custom));if(source==null && custom.startsWith("kfc/images/"))source=asset(custom);}catch(Exception ignored){}
        if(source==null){for(String stem:new String[]{code,slug(code),slug(name)}){if(stem==null||stem.isEmpty())continue;for(String ext:new String[]{".png",".jpg",".jpeg"}){source=asset("kfc/images/mon/"+stem+ext);if(source!=null)break;}if(source!=null)break;}}
        if(source==null)source=illustration(code,name);
        BufferedImage result=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);Graphics2D g=result.createGraphics();quality(g);g.setColor(new Color(250,246,242));g.fillRoundRect(0,0,w,h,12,12);g.clip(new RoundRectangle2D.Double(0,0,w,h,12,12));drawContain(g,source,3,3,w-6,h-6);g.dispose();ImageIcon icon=new ImageIcon(result);CACHE.put(key,icon);return icon;
    }
    /** Hình minh họa dự phòng, không giả làm ảnh chụp món ăn chính thức. */
    static BufferedImage illustration(String code,String name){
        String n=slug(name),c=code==null?"":code.toUpperCase(Locale.ROOT);BufferedImage im=new BufferedImage(320,240,BufferedImage.TYPE_INT_ARGB);Graphics2D g=im.createGraphics();quality(g);
        g.setPaint(new GradientPaint(0,0,new Color(255,249,242),320,240,new Color(245,228,220)));g.fillRect(0,0,320,240);
        g.setColor(new Color(228,0,43,16));g.fillOval(185,-70,210,210);g.setColor(new Color(88,42,31,28));g.fillOval(40,179,240,32);g.setColor(Color.WHITE);g.fillOval(38,144,244,51);g.setColor(new Color(226,215,204));g.drawOval(38,144,244,51);
        if(c.equals("M003")||c.equals("M004")||n.contains("burger")){burger(g,65,56,190,c.equals("M004"));}
        else if(c.equals("M005")||n.contains("khoai")){fries(g,98,35,126);}
        else if(c.equals("M006")||n.contains("pepsi")||n.contains("nuoc")){cup(g,114,35,95);}
        else if(c.equals("M009")||n.contains("kem")){ice(g,108,36,104);}
        else if(c.equals("M007")||c.equals("M008")||n.contains("combo")){cup(g,211,38,62);fries(g,35,73,78);bucket(g,95,62,134,c.equals("M008")?4:2);}
        else {chicken(g,65,87,102,c.equals("M002"),c.equals("M010"));chicken(g,158,66,107,c.equals("M002"),c.equals("M010"));}
        g.setColor(new Color(115,91,79));g.setFont(new Font("SansSerif",Font.PLAIN,12));g.drawString("MINH HỌA",14,226);g.dispose();return im;
    }
    private static void chicken(Graphics2D g,int x,int y,int s,boolean spicy,boolean bbq){Graphics2D a=(Graphics2D)g.create();a.translate(x,y);a.scale(s/100.0,s/100.0);a.rotate(-.28,50,50);a.setColor(new Color(255,240,213));a.fillRoundRect(43,59,19,47,12,12);a.fillOval(34,88,20,17);a.fillOval(53,89,20,17);a.setColor(bbq?new Color(153,54,30):spicy?new Color(189,79,24):new Color(207,119,32));a.fill(new RoundRectangle2D.Double(8,3,84,79,40,40));a.setColor(bbq?new Color(218,102,54):new Color(245,171,64));Random r=new Random(10);for(int i=0;i<28;i++){int xx=18+r.nextInt(62),yy=14+r.nextInt(53);a.fillOval(xx,yy,5+r.nextInt(9),4+r.nextInt(8));}a.dispose();}
    private static void burger(Graphics2D g,int x,int y,int s,boolean shrimp){Graphics2D a=(Graphics2D)g.create();a.translate(x,y);a.scale(s/190.0,s/190.0);a.setColor(new Color(220,140,53));a.fillRoundRect(8,96,174,34,30,30);a.setColor(new Color(109,52,29));a.fillRoundRect(5,77,180,25,23,23);a.setColor(shrimp?new Color(240,145,98):new Color(205,124,36));a.fillRoundRect(8,54,174,29,18,18);a.setColor(new Color(75,147,68));a.fillRoundRect(3,44,184,16,11,11);a.setColor(new Color(233,164,72));a.fill(new Arc2D.Double(7,-10,176,119,0,180,Arc2D.CHORD));a.setColor(new Color(255,237,173));for(int i=0;i<11;i++)a.fillOval(26+(i*23)%142,15+(i*7)%25,8,3);a.dispose();}
    private static void fries(Graphics2D g,int x,int y,int s){Graphics2D a=(Graphics2D)g.create();a.translate(x,y);a.scale(s/100.0,s/100.0);for(int i=0;i<9;i++){Graphics2D f=(Graphics2D)a.create();f.rotate((i-4)*.08,50,78);f.setColor(new Color(234,160,35));f.fillRoundRect(12+i*9,5+(i%3)*7,11,74,3,3);f.setColor(new Color(255,210,98));f.fillRect(14+i*9,7+(i%3)*7,4,64);f.dispose();}a.setColor(new Color(217,12,44));a.fillPolygon(new int[]{4,96,84,17},new int[]{50,50,112,112},4);a.setColor(new Color(253,248,234));a.fillRoundRect(36,72,28,22,8,8);a.dispose();}
    private static void cup(Graphics2D g,int x,int y,int s){Graphics2D a=(Graphics2D)g.create();a.translate(x,y);a.scale(s/100.0,s/100.0);a.setColor(new Color(219,19,48));a.fillRoundRect(61,0,7,45,4,4);a.setColor(new Color(34,75,156));a.fillPolygon(new int[]{6,94,80,20},new int[]{28,28,153,153},4);a.setColor(new Color(236,239,245));a.fillRoundRect(0,23,100,13,7,7);a.setColor(Color.WHITE);a.fillOval(30,64,40,40);a.setColor(new Color(212,25,47));a.fillArc(32,66,36,36,0,180);a.setColor(new Color(39,86,165));a.fillArc(32,66,36,36,180,180);a.dispose();}
    private static void ice(Graphics2D g,int x,int y,int s){Graphics2D a=(Graphics2D)g.create();a.translate(x,y);a.scale(s/100.0,s/100.0);a.setColor(new Color(192,42,48));a.fillPolygon(new int[]{3,97,80,20},new int[]{81,81,141,141},4);a.setColor(new Color(255,252,231));a.fillOval(2,54,96,37);a.fillOval(14,33,72,35);a.fillOval(26,12,51,40);a.fillOval(39,0,24,30);a.setColor(new Color(225,209,184));a.drawArc(15,34,70,28,185,160);a.drawArc(5,58,90,27,185,160);a.dispose();}
    private static void bucket(Graphics2D g,int x,int y,int s,int pieces){Graphics2D a=(Graphics2D)g.create();a.translate(x,y);a.scale(s/134.0,s/134.0);for(int i=0;i<pieces;i++)chicken(a,10+i*(pieces==4?25:58),-15+(i%2)*9,58,false,false);a.setColor(Color.WHITE);a.fillPolygon(new int[]{5,129,115,19},new int[]{34,34,115,115},4);a.setColor(new Color(221,18,49));for(int i=0;i<3;i++)a.fillPolygon(new int[]{15+i*42,34+i*42,37+i*32,24+i*32},new int[]{38,38,115,115},4);a.setColor(new Color(226,220,214));a.fillRoundRect(0,30,134,12,6,6);a.dispose();}
}

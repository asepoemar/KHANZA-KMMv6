/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgLhtBiaya.java
 *
 * Created on 12 Jul 10, 16:21:34
 */

package rekammedis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.akses;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.HyperlinkEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import simrskhanza.DlgCariPasien;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;
import java.util.Base64;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import javax.swing.SwingUtilities;
import org.apache.hc.core5.http.io.entity.StringEntity;



/**
 *
 * @author windiarto
 */
public final class RMRiwayatPenunjang extends javax.swing.JDialog {    
    private validasi Valid=new validasi();    
    private final sekuel Sequel=new sekuel();
//    private final DefaultTableModel tabModeRegistrasi;
    private PreparedStatement ps,ps2;
    private ResultSet rs,rs2,rs3,rs4;
    private Connection koneksi=koneksiDB.condb();
    private int i=0,urut=0,w=0,s=0,urutdpjp=0;
    private double biayaperawatan=0;
    private String kddpjp="",dpjp="",json,dokterrujukan="",polirujukan="",file="";
    private StringBuilder htmlContent;
    private HttpClient http = new HttpClient();
    private GetMethod get;
    private boolean esign=false,sertisign=false;
    private ObjectMapper mapper= new ObjectMapper();
    private JsonNode root;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean ceksukses = false;

    /** Creates new form DlgLhtBiaya
     * @param parent
     * @param modal */
    public RMRiwayatPenunjang(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(885,674);
        WindowPhrase.setSize(320,100);
        WindowURLSertisign.setSize(570,100);
        
//        tabModeRegistrasi=new DefaultTableModel(null,new Object[]{
//                "No.","No.Rawat","Tanggal","Jam","Kd.Dokter","Dokter Dituju/DPJP","Umur","Poliklinik/Kamar","Jenis Bayar"
//            }){
//             @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
//        };
//        tbRegistrasi.setModel(tabModeRegistrasi);
//
//        tbRegistrasi.setPreferredScrollableViewportSize(new Dimension(800,800));
//        tbRegistrasi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//
//        for (i = 0; i < 9; i++) {
//            TableColumn column = tbRegistrasi.getColumnModel().getColumn(i);
//            if(i==0){
//                column.setPreferredWidth(25);
//            }else if(i==1){
//                column.setPreferredWidth(110);
//            }else if(i==2){
//                column.setPreferredWidth(70);
//            }else if(i==3){
//                column.setPreferredWidth(60);
//            }else if(i==4){
//                column.setPreferredWidth(70);
//            }else if(i==5){
//                column.setPreferredWidth(250);   
//            }else if(i==6){
//                column.setPreferredWidth(40);
//            }else if(i==7){
//                column.setPreferredWidth(200);
//            }else if(i==8){
//                column.setPreferredWidth(110);
//            }
//        }
//        tbRegistrasi.setDefaultRenderer(Object.class, new WarnaTable());
//        
//        NoRM.setDocument(new batasInput((byte)20).getKata(NoRM));
//        NoRawat.setDocument(new batasInput((byte)20).getKata(NoRawat));
        
        HTMLEditorKit kit = new HTMLEditorKit();
        LoadHTMLRiwayatPerawatan.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}.isi a{text-decoration:none;color:#8b9b95;padding:0 0 0 0px;font-family: Tahoma;font-size: 8.5px;border: white;}");
        Document doc = kit.createDefaultDocument();
        LoadHTMLRiwayatPerawatan.setDocument(doc);
        LoadHTMLRiwayatPerawatan.setEditable(false);
        LoadHTMLRiwayatPerawatan.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
              Desktop desktop = Desktop.getDesktop();
              try {
                desktop.browse(e.getURL().toURI());
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            }
        });
        
        ChkAccor.setSelected(false);
        isMenu();
    }    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        Pekerjaan = new widget.TextBox();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnGeneratePDF = new javax.swing.JMenuItem();
        MnGeneratePDFESign = new javax.swing.JMenuItem();
        MnGeneratePDFSertiSign = new javax.swing.JMenuItem();
        WindowPhrase = new javax.swing.JDialog();
        internalFrame8 = new widget.InternalFrame();
        jLabel42 = new widget.Label();
        panelisi5 = new widget.panelisi();
        BtnClosePhrase = new widget.Button();
        BtnSimpanTandaTangan = new widget.Button();
        jLabel39 = new widget.Label();
        Phrase = new widget.PasswordBox();
        ChkTampilPhrase = new widget.CekBox();
        Tanggal = new widget.Tanggal();
        WindowURLSertisign = new javax.swing.JDialog();
        internalFrame9 = new widget.InternalFrame();
        jLabel43 = new widget.Label();
        panelisi6 = new widget.panelisi();
        BtnCloseUrl = new widget.Button();
        BtnBukaURL = new widget.Button();
        jLabel40 = new widget.Label();
        URLSertisign = new widget.TextBox();
        BtnDownloadFile = new widget.Button();
        BtnDownloadBukaFile = new widget.Button();
        internalFrame1 = new widget.InternalFrame();
        panelGlass5 = new widget.panelisi();
        R1 = new widget.RadioButton();
        R2 = new widget.RadioButton();
        R3 = new widget.RadioButton();
        Tgl1 = new widget.Tanggal();
        label18 = new widget.Label();
        Tgl2 = new widget.Tanggal();
        R4 = new widget.RadioButton();
        NoRawat = new widget.TextBox();
        BtnCari1 = new widget.Button();
        label19 = new widget.Label();
        BtnPrint = new widget.Button();
        BtnKeluar = new widget.Button();
        TabRawat = new javax.swing.JTabbedPane();
        internalFrame2 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        LoadHTMLRiwayatPerawatan = new widget.editorpane();
        PanelAccor = new widget.PanelBiasa();
        ChkAccor = new widget.CekBox();
        ScrollMenu = new widget.ScrollPane();
        FormMenu = new widget.PanelBiasa();
        chkSemua = new widget.CekBox();
        chkDiagnosaPenyakit = new widget.CekBox();
        chkProsedurTindakan = new widget.CekBox();
        chkHasilPemeriksaanUSG = new widget.CekBox();
        chkHasilPemeriksaanUSGUrologi = new widget.CekBox();
        chkHasilPemeriksaanUSGNeonatus = new widget.CekBox();
        chkHasilPemeriksaanUSGGynecologi = new widget.CekBox();
        chkHasilPemeriksaanEKG = new widget.CekBox();
        chkHasilPemeriksaanTreadmill = new widget.CekBox();
        chkHasilPemeriksaanSlitLamp = new widget.CekBox();
        chkHasilPemeriksaanOCT = new widget.CekBox();
        chkHasilPemeriksaanEcho = new widget.CekBox();
        chkHasilPemeriksaanEchoPediatrik = new widget.CekBox();
        chkHasilPemeriksaanEndoskopiFaringLaring = new widget.CekBox();
        chkHasilPemeriksaanEndoskopiHidung = new widget.CekBox();
        chkHasilPemeriksaanEndoskopiTelinga = new widget.CekBox();
        chkBerkasDigital = new widget.CekBox();
        chkPemeriksaanRadiologi = new widget.CekBox();
        chkPemeriksaanLaborat = new widget.CekBox();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        FormInput = new widget.panelisi();
        label17 = new widget.Label();
        NoRM = new widget.TextBox();
        NmPasien = new widget.TextBox();
        BtnPasien = new widget.Button();
        label20 = new widget.Label();
        Jk = new widget.TextBox();
        label21 = new widget.Label();
        TempatLahir = new widget.TextBox();
        label22 = new widget.Label();
        Alamat = new widget.TextBox();
        label23 = new widget.Label();
        GD = new widget.TextBox();
        label24 = new widget.Label();
        IbuKandung = new widget.TextBox();
        TanggalLahir = new widget.TextBox();
        label25 = new widget.Label();
        Agama = new widget.TextBox();
        StatusNikah = new widget.TextBox();
        label26 = new widget.Label();
        Pendidikan = new widget.TextBox();
        label27 = new widget.Label();
        label28 = new widget.Label();
        Bahasa = new widget.TextBox();
        label29 = new widget.Label();
        CacatFisik = new widget.TextBox();

        Pekerjaan.setEditable(false);
        Pekerjaan.setName("Pekerjaan"); // NOI18N
        Pekerjaan.setPreferredSize(new java.awt.Dimension(100, 23));

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnGeneratePDF.setBackground(new java.awt.Color(255, 255, 254));
        MnGeneratePDF.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnGeneratePDF.setForeground(new java.awt.Color(50, 50, 50));
        MnGeneratePDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnGeneratePDF.setText("Jadikan File PDF");
        MnGeneratePDF.setName("MnGeneratePDF"); // NOI18N
        MnGeneratePDF.setPreferredSize(new java.awt.Dimension(220, 26));
        MnGeneratePDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnGeneratePDFActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnGeneratePDF);

        MnGeneratePDFESign.setBackground(new java.awt.Color(255, 255, 254));
        MnGeneratePDFESign.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnGeneratePDFESign.setForeground(new java.awt.Color(50, 50, 50));
        MnGeneratePDFESign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnGeneratePDFESign.setText("Jadikan File PDF E-Sign");
        MnGeneratePDFESign.setName("MnGeneratePDFESign"); // NOI18N
        MnGeneratePDFESign.setPreferredSize(new java.awt.Dimension(220, 26));
        MnGeneratePDFESign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnGeneratePDFESignActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnGeneratePDFESign);

        MnGeneratePDFSertiSign.setBackground(new java.awt.Color(255, 255, 254));
        MnGeneratePDFSertiSign.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnGeneratePDFSertiSign.setForeground(new java.awt.Color(50, 50, 50));
        MnGeneratePDFSertiSign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnGeneratePDFSertiSign.setText("Jadikan File PDF SertiSign");
        MnGeneratePDFSertiSign.setName("MnGeneratePDFSertiSign"); // NOI18N
        MnGeneratePDFSertiSign.setPreferredSize(new java.awt.Dimension(220, 26));
        MnGeneratePDFSertiSign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnGeneratePDFSertiSignActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnGeneratePDFSertiSign);

        WindowPhrase.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowPhrase.setModal(true);
        WindowPhrase.setName("WindowPhrase"); // NOI18N
        WindowPhrase.setUndecorated(true);
        WindowPhrase.setResizable(false);

        internalFrame8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ E-Sign / Tanda Tangan Elektronik ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame8.setName("internalFrame8"); // NOI18N
        internalFrame8.setLayout(new java.awt.BorderLayout());

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel42.setText("%");
        jLabel42.setName("jLabel42"); // NOI18N
        internalFrame8.add(jLabel42, java.awt.BorderLayout.CENTER);

        panelisi5.setName("panelisi5"); // NOI18N
        panelisi5.setPreferredSize(new java.awt.Dimension(100, 44));
        panelisi5.setLayout(null);

        BtnClosePhrase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); // NOI18N
        BtnClosePhrase.setMnemonic('U');
        BtnClosePhrase.setText("Batal");
        BtnClosePhrase.setToolTipText("Alt+U");
        BtnClosePhrase.setName("BtnClosePhrase"); // NOI18N
        BtnClosePhrase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnClosePhraseActionPerformed(evt);
            }
        });
        panelisi5.add(BtnClosePhrase);
        BtnClosePhrase.setBounds(200, 40, 100, 30);

        BtnSimpanTandaTangan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpanTandaTangan.setMnemonic('S');
        BtnSimpanTandaTangan.setText("Simpan");
        BtnSimpanTandaTangan.setToolTipText("Alt+S");
        BtnSimpanTandaTangan.setName("BtnSimpanTandaTangan"); // NOI18N
        BtnSimpanTandaTangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanTandaTanganActionPerformed(evt);
            }
        });
        panelisi5.add(BtnSimpanTandaTangan);
        BtnSimpanTandaTangan.setBounds(10, 40, 100, 30);

        jLabel39.setText("Masukkan Passphrase :");
        jLabel39.setName("jLabel39"); // NOI18N
        panelisi5.add(jLabel39);
        jLabel39.setBounds(0, 10, 125, 23);

        Phrase.setForeground(new java.awt.Color(0, 0, 0));
        Phrase.setName("Phrase"); // NOI18N
        Phrase.setOpaque(true);
        panelisi5.add(Phrase);
        Phrase.setBounds(129, 10, 146, 23);

        ChkTampilPhrase.setBorder(null);
        ChkTampilPhrase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/matatutup.png"))); // NOI18N
        ChkTampilPhrase.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkTampilPhrase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkTampilPhrase.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkTampilPhrase.setIconTextGap(1);
        ChkTampilPhrase.setName("ChkTampilPhrase"); // NOI18N
        ChkTampilPhrase.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/matabuka.png"))); // NOI18N
        ChkTampilPhrase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkTampilPhraseActionPerformed(evt);
            }
        });
        panelisi5.add(ChkTampilPhrase);
        ChkTampilPhrase.setBounds(275, 10, 23, 23);

        internalFrame8.add(panelisi5, java.awt.BorderLayout.CENTER);

        WindowPhrase.getContentPane().add(internalFrame8, java.awt.BorderLayout.CENTER);

        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "05-05-2026 21:38:59" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);

        WindowURLSertisign.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowURLSertisign.setModal(true);
        WindowURLSertisign.setName("WindowURLSertisign"); // NOI18N
        WindowURLSertisign.setUndecorated(true);
        WindowURLSertisign.setResizable(false);

        internalFrame9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ URL File Hasil Tanda Tangan Sertisign ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame9.setName("internalFrame9"); // NOI18N
        internalFrame9.setLayout(new java.awt.BorderLayout());

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel43.setText("%");
        jLabel43.setName("jLabel43"); // NOI18N
        internalFrame9.add(jLabel43, java.awt.BorderLayout.CENTER);

        panelisi6.setName("panelisi6"); // NOI18N
        panelisi6.setPreferredSize(new java.awt.Dimension(100, 44));
        panelisi6.setLayout(null);

        BtnCloseUrl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); // NOI18N
        BtnCloseUrl.setMnemonic('T');
        BtnCloseUrl.setText("Tutup");
        BtnCloseUrl.setToolTipText("Alt+T");
        BtnCloseUrl.setName("BtnCloseUrl"); // NOI18N
        BtnCloseUrl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseUrlActionPerformed(evt);
            }
        });
        panelisi6.add(BtnCloseUrl);
        BtnCloseUrl.setBounds(450, 40, 100, 30);

        BtnBukaURL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnBukaURL.setMnemonic('B');
        BtnBukaURL.setText("Buka URL");
        BtnBukaURL.setToolTipText("Alt+B");
        BtnBukaURL.setName("BtnBukaURL"); // NOI18N
        BtnBukaURL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBukaURLActionPerformed(evt);
            }
        });
        panelisi6.add(BtnBukaURL);
        BtnBukaURL.setBounds(10, 40, 105, 30);

        jLabel40.setText("URL :");
        jLabel40.setName("jLabel40"); // NOI18N
        panelisi6.add(jLabel40);
        jLabel40.setBounds(0, 10, 40, 23);

        URLSertisign.setEditable(false);
        URLSertisign.setHighlighter(null);
        URLSertisign.setName("URLSertisign"); // NOI18N
        panelisi6.add(URLSertisign);
        URLSertisign.setBounds(44, 10, 505, 23);

        BtnDownloadFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnDownloadFile.setMnemonic('D');
        BtnDownloadFile.setText("Download File");
        BtnDownloadFile.setToolTipText("Alt+D");
        BtnDownloadFile.setName("BtnDownloadFile"); // NOI18N
        BtnDownloadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDownloadFileActionPerformed(evt);
            }
        });
        panelisi6.add(BtnDownloadFile);
        BtnDownloadFile.setBounds(125, 40, 130, 30);

        BtnDownloadBukaFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/preview-16x16.png"))); // NOI18N
        BtnDownloadBukaFile.setMnemonic('F');
        BtnDownloadBukaFile.setText("Download & Buka File");
        BtnDownloadBukaFile.setToolTipText("Alt+F");
        BtnDownloadBukaFile.setName("BtnDownloadBukaFile"); // NOI18N
        BtnDownloadBukaFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDownloadBukaFileActionPerformed(evt);
            }
        });
        panelisi6.add(BtnDownloadBukaFile);
        BtnDownloadBukaFile.setBounds(265, 40, 175, 30);

        internalFrame9.add(panelisi6, java.awt.BorderLayout.CENTER);

        WindowURLSertisign.getContentPane().add(internalFrame9, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Riwayat/Rincian Tindakan/Terapi Pasien ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass5.setName("panelGlass5"); // NOI18N
        panelGlass5.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        R1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R1);
        R1.setSelected(true);
        R1.setText("5 Riwayat Terakhir");
        R1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R1.setName("R1"); // NOI18N
        R1.setPreferredSize(new java.awt.Dimension(120, 23));
        panelGlass5.add(R1);

        R2.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R2);
        R2.setText("Semua Riwayat");
        R2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R2.setName("R2"); // NOI18N
        R2.setPreferredSize(new java.awt.Dimension(104, 23));
        panelGlass5.add(R2);

        R3.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R3);
        R3.setText("Tanggal :");
        R3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R3.setName("R3"); // NOI18N
        R3.setPreferredSize(new java.awt.Dimension(75, 23));
        panelGlass5.add(R3);

        Tgl1.setDisplayFormat("dd-MM-yyyy");
        Tgl1.setName("Tgl1"); // NOI18N
        Tgl1.setPreferredSize(new java.awt.Dimension(90, 23));
        Tgl1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl1KeyPressed(evt);
            }
        });
        panelGlass5.add(Tgl1);

        label18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label18.setText("s.d.");
        label18.setName("label18"); // NOI18N
        label18.setPreferredSize(new java.awt.Dimension(25, 23));
        panelGlass5.add(label18);

        Tgl2.setDisplayFormat("dd-MM-yyyy");
        Tgl2.setName("Tgl2"); // NOI18N
        Tgl2.setPreferredSize(new java.awt.Dimension(90, 23));
        Tgl2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl2KeyPressed(evt);
            }
        });
        panelGlass5.add(Tgl2);

        R4.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R4);
        R4.setText("Nomor :");
        R4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R4.setName("R4"); // NOI18N
        R4.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass5.add(R4);

        NoRawat.setName("NoRawat"); // NOI18N
        NoRawat.setPreferredSize(new java.awt.Dimension(135, 23));
        NoRawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRawatKeyPressed(evt);
            }
        });
        panelGlass5.add(NoRawat);

        BtnCari1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari1.setMnemonic('2');
        BtnCari1.setToolTipText("Alt+2");
        BtnCari1.setName("BtnCari1"); // NOI18N
        BtnCari1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCari1ActionPerformed(evt);
            }
        });
        panelGlass5.add(BtnCari1);

        label19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label19.setName("label19"); // NOI18N
        label19.setPreferredSize(new java.awt.Dimension(15, 23));
        panelGlass5.add(label19);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        panelGlass5.add(BtnPrint);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass5.add(BtnKeluar);

        internalFrame1.add(panelGlass5, java.awt.BorderLayout.PAGE_END);

        TabRawat.setBackground(new java.awt.Color(255, 255, 254));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        internalFrame2.setBackground(new java.awt.Color(235, 255, 235));
        internalFrame2.setBorder(null);
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        LoadHTMLRiwayatPerawatan.setBorder(null);
        LoadHTMLRiwayatPerawatan.setComponentPopupMenu(jPopupMenu1);
        LoadHTMLRiwayatPerawatan.setName("LoadHTMLRiwayatPerawatan"); // NOI18N
        Scroll.setViewportView(LoadHTMLRiwayatPerawatan);

        internalFrame2.add(Scroll, java.awt.BorderLayout.CENTER);

        PanelAccor.setBackground(new java.awt.Color(255, 255, 255));
        PanelAccor.setName("PanelAccor"); // NOI18N
        PanelAccor.setPreferredSize(new java.awt.Dimension(275, 43));
        PanelAccor.setLayout(new java.awt.BorderLayout());

        ChkAccor.setBackground(new java.awt.Color(255, 250, 250));
        ChkAccor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 255, 248)));
        ChkAccor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.setFocusable(false);
        ChkAccor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkAccor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkAccor.setName("ChkAccor"); // NOI18N
        ChkAccor.setPreferredSize(new java.awt.Dimension(15, 20));
        ChkAccor.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkAccorActionPerformed(evt);
            }
        });
        PanelAccor.add(ChkAccor, java.awt.BorderLayout.EAST);

        ScrollMenu.setBorder(null);
        ScrollMenu.setName("ScrollMenu"); // NOI18N
        ScrollMenu.setOpaque(true);
        ScrollMenu.setPreferredSize(new java.awt.Dimension(255, 1220));

        FormMenu.setBackground(new java.awt.Color(255, 255, 255));
        FormMenu.setBorder(null);
        FormMenu.setName("FormMenu"); // NOI18N
        FormMenu.setPreferredSize(new java.awt.Dimension(255, 4605));
        FormMenu.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 1));

        chkSemua.setSelected(true);
        chkSemua.setText("Semua");
        chkSemua.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkSemua.setName("chkSemua"); // NOI18N
        chkSemua.setOpaque(false);
        chkSemua.setPreferredSize(new java.awt.Dimension(245, 22));
        chkSemua.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkSemuaItemStateChanged(evt);
            }
        });
        FormMenu.add(chkSemua);

        chkDiagnosaPenyakit.setSelected(true);
        chkDiagnosaPenyakit.setText("Diagnosa/Penyakit (ICD 10)");
        chkDiagnosaPenyakit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkDiagnosaPenyakit.setName("chkDiagnosaPenyakit"); // NOI18N
        chkDiagnosaPenyakit.setOpaque(false);
        chkDiagnosaPenyakit.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkDiagnosaPenyakit);

        chkProsedurTindakan.setSelected(true);
        chkProsedurTindakan.setText("Prosedur/Tidakan (ICD 9)");
        chkProsedurTindakan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkProsedurTindakan.setName("chkProsedurTindakan"); // NOI18N
        chkProsedurTindakan.setOpaque(false);
        chkProsedurTindakan.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkProsedurTindakan);

        chkHasilPemeriksaanUSG.setSelected(true);
        chkHasilPemeriksaanUSG.setText("Hasil USG Kandungan");
        chkHasilPemeriksaanUSG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilPemeriksaanUSG.setName("chkHasilPemeriksaanUSG"); // NOI18N
        chkHasilPemeriksaanUSG.setOpaque(false);
        chkHasilPemeriksaanUSG.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilPemeriksaanUSG);

        chkHasilPemeriksaanUSGUrologi.setSelected(true);
        chkHasilPemeriksaanUSGUrologi.setText("Hasil USG Urologi");
        chkHasilPemeriksaanUSGUrologi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilPemeriksaanUSGUrologi.setName("chkHasilPemeriksaanUSGUrologi"); // NOI18N
        chkHasilPemeriksaanUSGUrologi.setOpaque(false);
        chkHasilPemeriksaanUSGUrologi.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilPemeriksaanUSGUrologi);

        chkHasilPemeriksaanUSGNeonatus.setSelected(true);
        chkHasilPemeriksaanUSGNeonatus.setText("Hasil USG Neonatus");
        chkHasilPemeriksaanUSGNeonatus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilPemeriksaanUSGNeonatus.setName("chkHasilPemeriksaanUSGNeonatus"); // NOI18N
        chkHasilPemeriksaanUSGNeonatus.setOpaque(false);
        chkHasilPemeriksaanUSGNeonatus.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilPemeriksaanUSGNeonatus);

        chkHasilPemeriksaanUSGGynecologi.setSelected(true);
        chkHasilPemeriksaanUSGGynecologi.setText("Hasil USG Gynecologi");
        chkHasilPemeriksaanUSGGynecologi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilPemeriksaanUSGGynecologi.setName("chkHasilPemeriksaanUSGGynecologi"); // NOI18N
        chkHasilPemeriksaanUSGGynecologi.setOpaque(false);
        chkHasilPemeriksaanUSGGynecologi.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilPemeriksaanUSGGynecologi);

        chkHasilPemeriksaanEKG.setSelected(true);
        chkHasilPemeriksaanEKG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilPemeriksaanEKG.setLabel("Hasil Pemeriksaan EKG");
        chkHasilPemeriksaanEKG.setName("chkHasilPemeriksaanEKG"); // NOI18N
        chkHasilPemeriksaanEKG.setOpaque(false);
        chkHasilPemeriksaanEKG.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilPemeriksaanEKG);

        chkHasilPemeriksaanTreadmill.setSelected(true);
        chkHasilPemeriksaanTreadmill.setText("Hasil Pemeriksaan Treadmill");
        chkHasilPemeriksaanTreadmill.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilPemeriksaanTreadmill.setName("chkHasilPemeriksaanTreadmill"); // NOI18N
        chkHasilPemeriksaanTreadmill.setOpaque(false);
        chkHasilPemeriksaanTreadmill.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilPemeriksaanTreadmill);

        chkHasilPemeriksaanSlitLamp.setSelected(true);
        chkHasilPemeriksaanSlitLamp.setText("Hasil Pemeriksaan Slit Lamp");
        chkHasilPemeriksaanSlitLamp.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilPemeriksaanSlitLamp.setName("chkHasilPemeriksaanSlitLamp"); // NOI18N
        chkHasilPemeriksaanSlitLamp.setOpaque(false);
        chkHasilPemeriksaanSlitLamp.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilPemeriksaanSlitLamp);

        chkHasilPemeriksaanOCT.setSelected(true);
        chkHasilPemeriksaanOCT.setText("Hasil Pemeriksaan OCT");
        chkHasilPemeriksaanOCT.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilPemeriksaanOCT.setName("chkHasilPemeriksaanOCT"); // NOI18N
        chkHasilPemeriksaanOCT.setOpaque(false);
        chkHasilPemeriksaanOCT.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilPemeriksaanOCT);

        chkHasilPemeriksaanEcho.setSelected(true);
        chkHasilPemeriksaanEcho.setText("Hasil Pemeriksaan ECHO");
        chkHasilPemeriksaanEcho.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilPemeriksaanEcho.setName("chkHasilPemeriksaanEcho"); // NOI18N
        chkHasilPemeriksaanEcho.setOpaque(false);
        chkHasilPemeriksaanEcho.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilPemeriksaanEcho);

        chkHasilPemeriksaanEchoPediatrik.setSelected(true);
        chkHasilPemeriksaanEchoPediatrik.setText("Hasil ECHO Pediatrik");
        chkHasilPemeriksaanEchoPediatrik.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilPemeriksaanEchoPediatrik.setName("chkHasilPemeriksaanEchoPediatrik"); // NOI18N
        chkHasilPemeriksaanEchoPediatrik.setOpaque(false);
        chkHasilPemeriksaanEchoPediatrik.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilPemeriksaanEchoPediatrik);

        chkHasilPemeriksaanEndoskopiFaringLaring.setSelected(true);
        chkHasilPemeriksaanEndoskopiFaringLaring.setText("Hasil Pemeriksaan Endoskopi Faring/Laring");
        chkHasilPemeriksaanEndoskopiFaringLaring.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilPemeriksaanEndoskopiFaringLaring.setName("chkHasilPemeriksaanEndoskopiFaringLaring"); // NOI18N
        chkHasilPemeriksaanEndoskopiFaringLaring.setOpaque(false);
        chkHasilPemeriksaanEndoskopiFaringLaring.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilPemeriksaanEndoskopiFaringLaring);

        chkHasilPemeriksaanEndoskopiHidung.setSelected(true);
        chkHasilPemeriksaanEndoskopiHidung.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilPemeriksaanEndoskopiHidung.setLabel("Hasil Pemeriksaan Endoskopi Hidung");
        chkHasilPemeriksaanEndoskopiHidung.setName("chkHasilPemeriksaanEndoskopiHidung"); // NOI18N
        chkHasilPemeriksaanEndoskopiHidung.setOpaque(false);
        chkHasilPemeriksaanEndoskopiHidung.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilPemeriksaanEndoskopiHidung);

        chkHasilPemeriksaanEndoskopiTelinga.setSelected(true);
        chkHasilPemeriksaanEndoskopiTelinga.setText("Hasil Pemeriksaan Endoskopi Telinga");
        chkHasilPemeriksaanEndoskopiTelinga.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilPemeriksaanEndoskopiTelinga.setName("chkHasilPemeriksaanEndoskopiTelinga"); // NOI18N
        chkHasilPemeriksaanEndoskopiTelinga.setOpaque(false);
        chkHasilPemeriksaanEndoskopiTelinga.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilPemeriksaanEndoskopiTelinga);

        chkBerkasDigital.setSelected(true);
        chkBerkasDigital.setText("Berkas Digital Perawatan");
        chkBerkasDigital.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkBerkasDigital.setName("chkBerkasDigital"); // NOI18N
        chkBerkasDigital.setOpaque(false);
        chkBerkasDigital.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkBerkasDigital);

        chkPemeriksaanRadiologi.setSelected(true);
        chkPemeriksaanRadiologi.setText("Pemeriksaan Radiologi");
        chkPemeriksaanRadiologi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkPemeriksaanRadiologi.setName("chkPemeriksaanRadiologi"); // NOI18N
        chkPemeriksaanRadiologi.setOpaque(false);
        chkPemeriksaanRadiologi.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkPemeriksaanRadiologi);

        chkPemeriksaanLaborat.setSelected(true);
        chkPemeriksaanLaborat.setText("Pemeriksaan Laborat");
        chkPemeriksaanLaborat.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkPemeriksaanLaborat.setName("chkPemeriksaanLaborat"); // NOI18N
        chkPemeriksaanLaborat.setOpaque(false);
        chkPemeriksaanLaborat.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkPemeriksaanLaborat);

        ScrollMenu.setViewportView(FormMenu);

        PanelAccor.add(ScrollMenu, java.awt.BorderLayout.CENTER);

        internalFrame2.add(PanelAccor, java.awt.BorderLayout.WEST);

        TabRawat.addTab("Riwayat Perawatan", internalFrame2);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        PanelInput.setBackground(new java.awt.Color(255, 255, 255));
        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('M');
        ChkInput.setSelected(true);
        ChkInput.setText(".: Tampilkan/Sembunyikan Data Pasien");
        ChkInput.setBorderPainted(true);
        ChkInput.setBorderPaintedFlat(true);
        ChkInput.setFocusable(false);
        ChkInput.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput.setName("ChkInput"); // NOI18N
        ChkInput.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputActionPerformed(evt);
            }
        });
        PanelInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 104));
        FormInput.setLayout(null);

        label17.setText("Pasien :");
        label17.setName("label17"); // NOI18N
        label17.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label17);
        label17.setBounds(5, 10, 55, 23);

        NoRM.setName("NoRM"); // NOI18N
        NoRM.setPreferredSize(new java.awt.Dimension(100, 23));
        NoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRMKeyPressed(evt);
            }
        });
        FormInput.add(NoRM);
        NoRM.setBounds(64, 10, 100, 23);

        NmPasien.setEditable(false);
        NmPasien.setName("NmPasien"); // NOI18N
        NmPasien.setPreferredSize(new java.awt.Dimension(220, 23));
        FormInput.add(NmPasien);
        NmPasien.setBounds(167, 10, 220, 23);

        BtnPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPasien.setMnemonic('3');
        BtnPasien.setToolTipText("Alt+3");
        BtnPasien.setName("BtnPasien"); // NOI18N
        BtnPasien.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPasienActionPerformed(evt);
            }
        });
        BtnPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPasienKeyPressed(evt);
            }
        });
        FormInput.add(BtnPasien);
        BtnPasien.setBounds(390, 10, 28, 23);

        label20.setText("J.K. :");
        label20.setName("label20"); // NOI18N
        label20.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label20);
        label20.setBounds(436, 10, 30, 23);

        Jk.setEditable(false);
        Jk.setName("Jk"); // NOI18N
        Jk.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Jk);
        Jk.setBounds(470, 10, 40, 23);

        label21.setText("Tempat & Tgl.Lahir :");
        label21.setName("label21"); // NOI18N
        label21.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label21);
        label21.setBounds(523, 10, 110, 23);

        TempatLahir.setEditable(false);
        TempatLahir.setName("TempatLahir"); // NOI18N
        TempatLahir.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(TempatLahir);
        TempatLahir.setBounds(637, 10, 140, 23);

        label22.setText("Alamat :");
        label22.setName("label22"); // NOI18N
        label22.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label22);
        label22.setBounds(5, 40, 55, 23);

        Alamat.setEditable(false);
        Alamat.setName("Alamat"); // NOI18N
        Alamat.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Alamat);
        Alamat.setBounds(64, 40, 354, 23);

        label23.setText("G.D. :");
        label23.setName("label23"); // NOI18N
        label23.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label23);
        label23.setBounds(436, 40, 30, 23);

        GD.setEditable(false);
        GD.setName("GD"); // NOI18N
        GD.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(GD);
        GD.setBounds(470, 40, 40, 23);

        label24.setText("Nama Ibu Kandung :");
        label24.setName("label24"); // NOI18N
        label24.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label24);
        label24.setBounds(523, 40, 110, 23);

        IbuKandung.setEditable(false);
        IbuKandung.setName("IbuKandung"); // NOI18N
        IbuKandung.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(IbuKandung);
        IbuKandung.setBounds(637, 40, 225, 23);

        TanggalLahir.setEditable(false);
        TanggalLahir.setName("TanggalLahir"); // NOI18N
        TanggalLahir.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(TanggalLahir);
        TanggalLahir.setBounds(779, 10, 83, 23);

        label25.setText("Agama :");
        label25.setName("label25"); // NOI18N
        label25.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label25);
        label25.setBounds(5, 70, 55, 23);

        Agama.setEditable(false);
        Agama.setName("Agama"); // NOI18N
        Agama.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Agama);
        Agama.setBounds(64, 70, 100, 23);

        StatusNikah.setEditable(false);
        StatusNikah.setName("StatusNikah"); // NOI18N
        StatusNikah.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(StatusNikah);
        StatusNikah.setBounds(245, 70, 100, 23);

        label26.setText("Stts.Nikah :");
        label26.setName("label26"); // NOI18N
        label26.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label26);
        label26.setBounds(176, 70, 65, 23);

        Pendidikan.setEditable(false);
        Pendidikan.setName("Pendidikan"); // NOI18N
        Pendidikan.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Pendidikan);
        Pendidikan.setBounds(429, 70, 80, 23);

        label27.setText("Pendidikan :");
        label27.setName("label27"); // NOI18N
        label27.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label27);
        label27.setBounds(355, 70, 70, 23);

        label28.setText("Bahasa :");
        label28.setName("label28"); // NOI18N
        label28.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label28);
        label28.setBounds(520, 70, 50, 23);

        Bahasa.setEditable(false);
        Bahasa.setName("Bahasa"); // NOI18N
        Bahasa.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Bahasa);
        Bahasa.setBounds(574, 70, 100, 23);

        label29.setText("Cacat Fisik :");
        label29.setName("label29"); // NOI18N
        label29.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label29);
        label29.setBounds(683, 70, 70, 23);

        CacatFisik.setEditable(false);
        CacatFisik.setName("CacatFisik"); // NOI18N
        CacatFisik.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(CacatFisik);
        CacatFisik.setBounds(757, 70, 105, 23);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,Tgl1,NoRM);}
}//GEN-LAST:event_BtnKeluarKeyPressed

private void NoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRMKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isPasien();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isPasien();
            BtnKeluar.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnPasienActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isPasien();
            BtnPrint.requestFocus();
        }
}//GEN-LAST:event_NoRMKeyPressed

private void BtnPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPasienActionPerformed
    if(akses.getpasien()==true){
        DlgCariPasien pasien=new DlgCariPasien(null,true);
        pasien.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(pasien.getTable().getSelectedRow()!= -1){                   
                    NoRM.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),0).toString());
                    NmPasien.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),1).toString());
                    Jk.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),3).toString());
                    TempatLahir.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),4).toString());
                    TanggalLahir.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),5).toString());
                    IbuKandung.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),6).toString());
                    Alamat.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),7).toString());
                    GD.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),8).toString());
                    StatusNikah.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),10).toString());
                    Agama.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),11).toString());
                    Pendidikan.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),15).toString());
                    Bahasa.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),26).toString());
                    CacatFisik.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),32).toString());
                }    
                NoRM.requestFocus();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        pasien.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    pasien.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        pasien.isCek();
        pasien.emptTeks();
        pasien.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        pasien.setLocationRelativeTo(internalFrame1);
        pasien.setVisible(true);
    }   
}//GEN-LAST:event_BtnPasienActionPerformed

private void BtnPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPasienKeyPressed
    //Valid.pindah(evt,Tgl2,TKd);
}//GEN-LAST:event_BtnPasienKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        if(NoRM.getText().trim().equals("")||NmPasien.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Pasien masih kosong...!!!");
        }else{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            switch (TabRawat.getSelectedIndex()) {
                case 0:
                    panggilLaporan(LoadHTMLRiwayatPerawatan.getText()); 
                    break;
                default:
                    break;
            }
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void Tgl1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl1KeyPressed
        Valid.pindah(evt, BtnKeluar, Tgl2);
    }//GEN-LAST:event_Tgl1KeyPressed

    private void Tgl2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl2KeyPressed
        Valid.pindah(evt, Tgl1,NoRM);
    }//GEN-LAST:event_Tgl2KeyPressed

    private void BtnCari1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari1ActionPerformed
        if (NoRM.getText().trim().isEmpty() || NmPasien.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pasien masih kosong...!!!");
            return;
        }

        switch (TabRawat.getSelectedIndex()) {
            case 0:
                esign = false;
                sertisign = false;
                runBackground(() -> tampilPerawatan());
                break;
            default:
                break;
        }
    }//GEN-LAST:event_BtnCari1ActionPerformed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        BtnCari1ActionPerformed(null);
    }//GEN-LAST:event_TabRawatMouseClicked

    private void ChkAccorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkAccorActionPerformed
        isMenu();
    }//GEN-LAST:event_ChkAccorActionPerformed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void NoRawatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRawatKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCari1ActionPerformed(null);
        }
    }//GEN-LAST:event_NoRawatKeyPressed

    private void MnGeneratePDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnGeneratePDFActionPerformed
        R4.setSelected(true);
        if(NoRawat.getText().equals("")){
            JOptionPane.showMessageDialog(null,"No.Rawat masih kosong...!!!");
            NoRawat.requestFocus();
        }else{
            try{
                esign=false;
                sertisign=false;
                tampilPerawatan();
                File g = new File("file.css");            
                BufferedWriter bg = new BufferedWriter(new FileWriter(g));
                bg.write(".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}.isi a{text-decoration:none;color:#8b9b95;padding:0 0 0 0px;font-family: Tahoma;font-size: 8.5px;border: white;}");
                bg.close();

                PdfWriter pdf = new PdfWriter("RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf");
                HtmlConverter.convertToPdf(
                    LoadHTMLRiwayatPerawatan.getText().replaceAll("<head>","<head><link href=\"file.css\" rel=\"stylesheet\" type=\"text/css\" />").
                          replaceAll("<body>",
                                     "<body>"+
                                        "<table width='100%' align='center' border='0' class='tbl_form' cellspacing='0' cellpadding='0'>" +
                                            "<tr>" +
                                                "<td width='15%' border='0'>" +
                                                    "<img width='50' height='50' src='data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(Sequel.cariGambar("select setting.logo from setting").readAllBytes())+"'/>" +
                                                "</td>" +
                                                "<td width='85%' border='0'>" +
                                                    "<center>" +
                                                        "<font color='000000' size='3'  face='Tahoma'>"+akses.getnamars()+"</font><br>"+
                                                        "<font color='000000' size='1'  face='Tahoma'>"+
                                                            akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br/>" +
                                                            akses.getkontakrs()+", E-mail : "+akses.getemailrs()+
                                                            "<br>RIWAYAT PERAWATAN" +
                                                        "</font> " +
                                                    "</center>" +
                                                "</td>" +
                                            "</tr>" +
                                        "</table><br>"+
                                        "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                           "<tr class='isi'>"+ 
                                             "<td valign='top' width='20%'>No.RM</td>"+
                                             "<td valign='top' width='1%' align='center'>:</td>"+
                                             "<td valign='top' width='79%'>"+NoRM.getText().trim()+"</td>"+
                                           "</tr>"+
                                           "<tr class='isi'>"+ 
                                             "<td valign='top' width='20%'>Nama Pasien</td>"+
                                             "<td valign='top' width='1%' align='center'>:</td>"+
                                             "<td valign='top' width='79%'>"+NmPasien.getText()+"</td>"+
                                           "</tr>"+
                                           "<tr class='isi'>"+ 
                                             "<td valign='top' width='20%'>Alamat</td>"+
                                             "<td valign='top' width='1%' align='center'>:</td>"+
                                             "<td valign='top' width='79%'>"+Alamat.getText()+"</td>"+
                                           "</tr>"+
                                           "<tr class='isi'>"+ 
                                             "<td valign='top' width='20%'>Jenis Kelamin</td>"+
                                             "<td valign='top' width='1%' align='center'>:</td>"+
                                             "<td valign='top' width='79%'>"+Jk.getText().replaceAll("L","Laki-Laki").replaceAll("P","Perempuan")+"</td>"+
                                           "</tr>"+
                                           "<tr class='isi'>"+ 
                                             "<td valign='top' width='20%'>Tempat & Tanggal Lahir</td>"+
                                             "<td valign='top' width='1%' align='center'>:</td>"+
                                             "<td valign='top' width='79%'>"+TempatLahir.getText()+" "+TanggalLahir.getText()+"</td>"+
                                           "</tr>"+
                                           "<tr class='isi'>"+ 
                                             "<td valign='top' width='20%'>Ibu Kandung</td>"+
                                             "<td valign='top' width='1%' align='center'>:</td>"+
                                             "<td valign='top' width='79%'>"+IbuKandung.getText()+"</td>"+
                                           "</tr>"+
                                           "<tr class='isi'>"+ 
                                             "<td valign='top' width='20%'>Golongan Darah</td>"+
                                             "<td valign='top' width='1%' align='center'>:</td>"+
                                             "<td valign='top' width='79%'>"+GD.getText()+"</td>"+
                                           "</tr>"+
                                           "<tr class='isi'>"+ 
                                             "<td valign='top' width='20%'>Status Nikah</td>"+
                                             "<td valign='top' width='1%' align='center'>:</td>"+
                                             "<td valign='top' width='79%'>"+StatusNikah.getText()+"</td>"+
                                           "</tr>"+
                                           "<tr class='isi'>"+ 
                                             "<td valign='top' width='20%'>Agama</td>"+
                                             "<td valign='top' width='1%' align='center'>:</td>"+
                                             "<td valign='top' width='79%'>"+Agama.getText()+"</td>"+
                                           "</tr>"+
                                           "<tr class='isi'>"+ 
                                             "<td valign='top' width='20%'>Pendidikan Terakhir</td>"+
                                             "<td valign='top' width='1%' align='center'>:</td>"+
                                             "<td valign='top' width='79%'>"+Pendidikan.getText()+"</td>"+
                                           "</tr>"+
                                           "<tr class='isi'>"+ 
                                             "<td valign='top' width='20%'>Bahasa Dipakai</td>"+
                                             "<td valign='top' width='1%' align='center'>:</td>"+
                                             "<td valign='top' width='79%'>"+Bahasa.getText()+"</td>"+
                                           "</tr>"+
                                           "<tr class='isi'>"+ 
                                             "<td valign='top' width='20%'>Cacat Fisik</td>"+
                                             "<td valign='top' width='1%' align='center'>:</td>"+
                                             "<td valign='top' width='79%'>"+CacatFisik.getText()+"</td>"+
                                           "</tr>"+
                                        "</table>"            
                          ).
                          replaceAll((getClass().getResource("/picture/"))+"","./gambar/"), pdf
                );
                File f = new File("RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf");   
                Desktop.getDesktop().browse(f.toURI());
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            }  
        }
    }//GEN-LAST:event_MnGeneratePDFActionPerformed

    private void MnGeneratePDFESignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnGeneratePDFESignActionPerformed
        R4.setSelected(true);
        if(NoRawat.getText().equals("")){
            Valid.textKosong(Phrase,"No.Rawat");
        }else{
            WindowPhrase.setAlwaysOnTop(true);
            WindowPhrase.setLocationRelativeTo(internalFrame1);
            WindowPhrase.setVisible(true);
        } 
    }//GEN-LAST:event_MnGeneratePDFESignActionPerformed

    private void BtnClosePhraseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnClosePhraseActionPerformed
        Phrase.setText("");
        WindowPhrase.dispose();
    }//GEN-LAST:event_BtnClosePhraseActionPerformed

    private void BtnSimpanTandaTanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanTandaTanganActionPerformed
        R4.setSelected(true);
        if(Phrase.getText().equals("")){
            Valid.textKosong(Phrase,"Phrase");
        }else{
            try{
                esign=true;
                sertisign=false;
                tampilPerawatan();
                if(esign==true){
                    File g = new File("file.css");            
                    BufferedWriter bg = new BufferedWriter(new FileWriter(g));
                    bg.write(".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}.isi a{text-decoration:none;color:#8b9b95;padding:0 0 0 0px;font-family: Tahoma;font-size: 8.5px;border: white;}");
                    bg.close();

                    PdfWriter pdf = new PdfWriter("RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf");
                    HtmlConverter.convertToPdf(
                        LoadHTMLRiwayatPerawatan.getText().replaceAll("<head>","<head><link href=\"file.css\" rel=\"stylesheet\" type=\"text/css\" />").
                              replaceAll("<body>",
                                         "<body>"+
                                            "<table width='100%' align='center' border='0' class='tbl_form' cellspacing='0' cellpadding='0'>" +
                                                "<tr>" +
                                                    "<td width='15%' border='0'>" +
                                                        "<img width='50' height='50' src='data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(Sequel.cariGambar("select setting.logo from setting").readAllBytes())+"'/>" +
                                                    "</td>" +
                                                    "<td width='85%' border='0'>" +
                                                        "<center>" +
                                                            "<font color='000000' size='3'  face='Tahoma'>"+akses.getnamars()+"</font><br>"+
                                                            "<font color='000000' size='1'  face='Tahoma'>"+
                                                                akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br/>" +
                                                                akses.getkontakrs()+", E-mail : "+akses.getemailrs()+
                                                                "<br>RIWAYAT PERAWATAN" +
                                                            "</font> " +
                                                        "</center>" +
                                                    "</td>" +
                                                "</tr>" +
                                            "</table><br>"+
                                            "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>No.RM</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+NoRM.getText().trim()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Nama Pasien</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+NmPasien.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Alamat</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+Alamat.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Jenis Kelamin</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+Jk.getText().replaceAll("L","Laki-Laki").replaceAll("P","Perempuan")+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Tempat & Tanggal Lahir</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+TempatLahir.getText()+" "+TanggalLahir.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Ibu Kandung</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+IbuKandung.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Golongan Darah</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+GD.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Status Nikah</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+StatusNikah.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Agama</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+Agama.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Pendidikan Terakhir</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+Pendidikan.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Bahasa Dipakai</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+Bahasa.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Cacat Fisik</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+CacatFisik.getText()+"</td>"+
                                               "</tr>"+
                                            "</table>"            
                              ).
                              replaceAll((getClass().getResource("/picture/"))+"","./gambar/"), pdf
                    );
                    System.out.println("Membuat ulang File RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf untuk dikirim ke server");
                    File f = new File("RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf");   
                    try {
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpPost post = new HttpPost(koneksiDB.URLAKSESFILEESIGN());
                        post.setHeader("Content-Type", "application/json");
                        post.addHeader("username", koneksiDB.USERNAMEAPIESIGN());
                        post.addHeader("password", koneksiDB.PASSAPIESIGN());
                        post.addHeader("url", koneksiDB.URLAPIESIGN());

                        byte[] fileContent = Files.readAllBytes(f.toPath());

                        json="{" +
                                 "\"file\":\""+Base64.getEncoder().encodeToString(fileContent)+"\"," +
                                 "\"nik\":\""+Sequel.cariIsi("select pegawai.no_ktp from pegawai where pegawai.nik=?", akses.getkode())+"\"," +
                                 "\"passphrase\":\""+Phrase.getText()+"\"," +
                                 "\"tampilan\":\"visible\"," +
                                 "\"image\":\"false\"," +
                                 "\"linkQR\":\"Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+". Ditandatangani secara elektronik oleh "+dpjp+" ID "+kddpjp+" Tanggal "+Tanggal.getSelectedItem().toString()+"\"," +
                                 "\"width\":\"70\"," +
                                 "\"height\":\"70\"," +
                                 "\"tag_koordinat\":\"#\"" +
                              "}";

                        System.out.println("URL Akses file :"+koneksiDB.URLAKSESFILEESIGN());
                        post.setEntity(new StringEntity(json));
                        try (CloseableHttpResponse response = httpClient.execute(post)) {
                            System.out.println("Response Status : " + response.getCode());
                            json=EntityUtils.toString(response.getEntity());
                            root = mapper.readTree(json);
                            if (response.getCode() == 200) {
                                try (FileOutputStream fos = new FileOutputStream(new File("RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf"))) {
                                    byte[] fileBytes = Base64.getDecoder().decode(root.path("response").asText());
                                    fos.write(fileBytes);
                                    WindowPhrase.dispose();
                                    JOptionPane.showMessageDialog(null,"Proses tanda tangan berhasil...");
                                    Desktop.getDesktop().browse(new File("RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf").toURI());
                                } catch (Exception e) {
                                    WindowPhrase.dispose();
                                    JOptionPane.showMessageDialog(null,"Gagal mengkonversi base64 ke file...");
                                    System.out.println("Notif : " +e);
                                }
                            } else {
                                WindowPhrase.dispose();
                                JOptionPane.showMessageDialog(null,"Code : "+root.path("metadata").path("code").asText()+" Pesan : "+root.path("metadata").path("message").asText());
                            }
                        } catch (IOException a) {
                            WindowPhrase.dispose();
                            System.out.println("Notifikasi : " + a);
                            JOptionPane.showMessageDialog(null,""+a);
                        }
                    } catch (Exception e) {
                        WindowPhrase.dispose();
                        System.out.println("Notifikasi : " + e);
                        JOptionPane.showMessageDialog(null,""+e);
                    }
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } 
        }
    }//GEN-LAST:event_BtnSimpanTandaTanganActionPerformed

    private void MnGeneratePDFSertiSignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnGeneratePDFSertiSignActionPerformed
        R4.setSelected(true);
        if(NoRawat.getText().equals("")){
            JOptionPane.showMessageDialog(null,"No.Rawat masih kosong...!!!");
            NoRawat.requestFocus();
        }else{
            try{
                esign=false;
                sertisign=true;
                tampilPerawatan();
                if(sertisign==true){
                    File g = new File("file.css");            
                    BufferedWriter bg = new BufferedWriter(new FileWriter(g));
                    bg.write(".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}.isi a{text-decoration:none;color:#8b9b95;padding:0 0 0 0px;font-family: Tahoma;font-size: 8.5px;border: white;}");
                    bg.close();

                    PdfWriter pdf = new PdfWriter("RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf");
                    HtmlConverter.convertToPdf(
                        LoadHTMLRiwayatPerawatan.getText().replaceAll("<head>","<head><link href=\"file.css\" rel=\"stylesheet\" type=\"text/css\" />").
                              replaceAll("<body>",
                                         "<body>"+
                                            "<table width='100%' align='center' border='0' class='tbl_form' cellspacing='0' cellpadding='0'>" +
                                                "<tr>" +
                                                    "<td width='15%' border='0'>" +
                                                        "<img width='50' height='50' src='data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(Sequel.cariGambar("select setting.logo from setting").readAllBytes())+"'/>" +
                                                    "</td>" +
                                                    "<td width='85%' border='0'>" +
                                                        "<center>" +
                                                            "<font color='000000' size='3'  face='Tahoma'>"+akses.getnamars()+"</font><br>"+
                                                            "<font color='000000' size='1'  face='Tahoma'>"+
                                                                akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br/>" +
                                                                akses.getkontakrs()+", E-mail : "+akses.getemailrs()+
                                                                "<br>RIWAYAT PERAWATAN" +
                                                            "</font> " +
                                                        "</center>" +
                                                    "</td>" +
                                                "</tr>" +
                                            "</table><br>"+
                                            "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>No.RM</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+NoRM.getText().trim()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Nama Pasien</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+NmPasien.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Alamat</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+Alamat.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Jenis Kelamin</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+Jk.getText().replaceAll("L","Laki-Laki").replaceAll("P","Perempuan")+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Tempat & Tanggal Lahir</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+TempatLahir.getText()+" "+TanggalLahir.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Ibu Kandung</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+IbuKandung.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Golongan Darah</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+GD.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Status Nikah</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+StatusNikah.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Agama</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+Agama.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Pendidikan Terakhir</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+Pendidikan.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Bahasa Dipakai</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+Bahasa.getText()+"</td>"+
                                               "</tr>"+
                                               "<tr class='isi'>"+ 
                                                 "<td valign='top' width='20%'>Cacat Fisik</td>"+
                                                 "<td valign='top' width='1%' align='center'>:</td>"+
                                                 "<td valign='top' width='79%'>"+CacatFisik.getText()+"</td>"+
                                               "</tr>"+
                                            "</table>"            
                              ).
                              replaceAll((getClass().getResource("/picture/"))+"","./gambar/"), pdf
                    );
                    File f = new File("RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf");  
                    try {
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpPost post = new HttpPost(koneksiDB.URLAPISERTISIGN());
                        post.addHeader("apikey",koneksiDB.APIKEYSERTISIGN());
                        post.addHeader("Accept","application/json");
                        json=Sequel.cariIsi("select dokter.email from dokter where dokter.kd_dokter=?", akses.getkode());
                        kddpjp=Sequel.cariIsi("select pegawai.no_ktp from pegawai where pegawai.nik=?", akses.getkode());
                        dpjp=Sequel.cariIsi("select pegawai.nama from pegawai where pegawai.nik=?", akses.getkode());
                        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create()
                            .addBinaryBody("document", f, ContentType.APPLICATION_PDF, f.getName())
                            .addTextBody("signer", json)
                            .addTextBody("keyword","#1A")
                            .addTextBody("qrValue","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+dpjp+"\nID "+kddpjp+"\n"+Tanggal.getSelectedItem().toString())
                            .addTextBody("w", "60")
                            .addTextBody("h", "60");
                        HttpEntity entity = entityBuilder.build();
                        post.setEntity(entity);
                        System.out.println("URL Kirim File : "+koneksiDB.URLAPISERTISIGN());
                        try (CloseableHttpResponse response = httpClient.execute(post)) {
                            if (response.getCode() == 200) {
                                json=EntityUtils.toString(response.getEntity());
                                System.out.println("Respon Kirim File : " + json);
                                root = mapper.readTree(json);
                                System.out.println("Id File : " +root.path("data").path("transaction_id").asText());
                                System.out.println("URL Callback File : "+koneksiDB.URLDOKUMENSERTISIGN()+"/"+root.path("data").path("transaction_id").asText()+".pdf");
                                URLSertisign.setText(koneksiDB.URLDOKUMENSERTISIGN()+"/"+root.path("data").path("transaction_id").asText()+".pdf");
                                WindowURLSertisign.setAlwaysOnTop(true);
                                WindowURLSertisign.setLocationRelativeTo(internalFrame1);
                                WindowURLSertisign.setVisible(true);
                            } else {
                                System.out.println("Notifikasi : " + EntityUtils.toString(response.getEntity()));
                            }
                        } catch (Exception a) {
                            System.out.println("Notifikasi : " + a);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : " + e);
                    }
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            }  
        }
    }//GEN-LAST:event_MnGeneratePDFSertiSignActionPerformed

    private void BtnCloseUrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseUrlActionPerformed
        URLSertisign.setText("");
        WindowURLSertisign.dispose();
    }//GEN-LAST:event_BtnCloseUrlActionPerformed

    private void BtnBukaURLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBukaURLActionPerformed
        try {
            Desktop.getDesktop().browse(new URI(URLSertisign.getText()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane,"File belum tersedia, silahkan tunggu beberapa saat lagi..!!");
            System.out.println("Notifikasi : " + e);
        }
    }//GEN-LAST:event_BtnBukaURLActionPerformed

    private void BtnDownloadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDownloadFileActionPerformed
        try {
            URL url = new URL(URLSertisign.getText());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            readableByteChannel.close();
            System.out.println("Download Selesai : " + "RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(rootPane,"File belum tersedia, silahkan tunggu & ulangi beberapa saat lagi..!!");
            System.out.println("Notifikasi : " + e);
        }
    }//GEN-LAST:event_BtnDownloadFileActionPerformed

    private void BtnDownloadBukaFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDownloadBukaFileActionPerformed
        try {
            URL url = new URL(URLSertisign.getText());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            readableByteChannel.close();
            System.out.println("Download Selesai : " + "RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf");
            Desktop.getDesktop().browse(new File("RPP"+NoRawat.getText().trim().replaceAll("/","")+".pdf").toURI());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(rootPane,"File belum tersedia, silahkan tunggu & ulangi beberapa saat lagi..!!");
            System.out.println("Notifikasi : " + e);
        }
    }//GEN-LAST:event_BtnDownloadBukaFileActionPerformed

    private void ChkTampilPhraseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkTampilPhraseActionPerformed
        if(ChkTampilPhrase.isSelected()==true){
            Phrase.setEchoChar((char) 0);
        }else{
            Phrase.setEchoChar('\u2022');
        }
    }//GEN-LAST:event_ChkTampilPhraseActionPerformed

    private void chkSemuaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkSemuaItemStateChanged
        if(chkSemua.isSelected()==true){
            chkDiagnosaPenyakit.setSelected(true);
            chkProsedurTindakan.setSelected(true);
            chkPemeriksaanRadiologi.setSelected(true);
            chkPemeriksaanLaborat.setSelected(true);
            chkBerkasDigital.setSelected(true);
            chkHasilPemeriksaanUSG.setSelected(true);
            chkHasilPemeriksaanUSGUrologi.setSelected(true);
            chkHasilPemeriksaanUSGGynecologi.setSelected(true);
            chkHasilPemeriksaanEKG.setSelected(true);
            chkHasilPemeriksaanUSGNeonatus.setSelected(true);
            chkHasilPemeriksaanEndoskopiFaringLaring.setSelected(true);
            chkHasilPemeriksaanEndoskopiHidung.setSelected(true);
            chkHasilPemeriksaanEndoskopiTelinga.setSelected(true);
            chkHasilPemeriksaanEcho.setSelected(true);
            chkHasilPemeriksaanSlitLamp.setSelected(true);
            chkHasilPemeriksaanOCT.setSelected(true);
            chkHasilPemeriksaanTreadmill.setSelected(true);
            chkHasilPemeriksaanEchoPediatrik.setSelected(true);
        }else{
            chkDiagnosaPenyakit.setSelected(false);
            chkProsedurTindakan.setSelected(false);
            chkPemeriksaanRadiologi.setSelected(false);
            chkPemeriksaanLaborat.setSelected(false);
            chkBerkasDigital.setSelected(false);
            chkHasilPemeriksaanUSG.setSelected(false);
            chkHasilPemeriksaanUSGUrologi.setSelected(false);
            chkHasilPemeriksaanUSGGynecologi.setSelected(false);
            chkHasilPemeriksaanEKG.setSelected(false);
            chkHasilPemeriksaanUSGNeonatus.setSelected(false);
            chkHasilPemeriksaanEndoskopiFaringLaring.setSelected(false);
            chkHasilPemeriksaanEndoskopiHidung.setSelected(false);
            chkHasilPemeriksaanEndoskopiTelinga.setSelected(false);
            chkHasilPemeriksaanEcho.setSelected(false);
            chkHasilPemeriksaanSlitLamp.setSelected(false);
            chkHasilPemeriksaanOCT.setSelected(false);
            chkHasilPemeriksaanTreadmill.setSelected(false);
            chkHasilPemeriksaanEchoPediatrik.setSelected(false);
        }
    }//GEN-LAST:event_chkSemuaItemStateChanged

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMRiwayatPenunjang dialog = new RMRiwayatPenunjang(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.TextBox Agama;
    private widget.TextBox Alamat;
    private widget.TextBox Bahasa;
    private widget.Button BtnBukaURL;
    private widget.Button BtnCari1;
    private widget.Button BtnClosePhrase;
    private widget.Button BtnCloseUrl;
    private widget.Button BtnDownloadBukaFile;
    private widget.Button BtnDownloadFile;
    private widget.Button BtnKeluar;
    private widget.Button BtnPasien;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpanTandaTangan;
    private widget.TextBox CacatFisik;
    private widget.CekBox ChkAccor;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkTampilPhrase;
    private widget.panelisi FormInput;
    private widget.PanelBiasa FormMenu;
    private widget.TextBox GD;
    private widget.TextBox IbuKandung;
    private widget.TextBox Jk;
    private widget.editorpane LoadHTMLRiwayatPerawatan;
    private javax.swing.JMenuItem MnGeneratePDF;
    private javax.swing.JMenuItem MnGeneratePDFESign;
    private javax.swing.JMenuItem MnGeneratePDFSertiSign;
    private widget.TextBox NmPasien;
    private widget.TextBox NoRM;
    private widget.TextBox NoRawat;
    private widget.PanelBiasa PanelAccor;
    private javax.swing.JPanel PanelInput;
    private widget.TextBox Pekerjaan;
    private widget.TextBox Pendidikan;
    private widget.PasswordBox Phrase;
    private widget.RadioButton R1;
    private widget.RadioButton R2;
    private widget.RadioButton R3;
    private widget.RadioButton R4;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane ScrollMenu;
    private widget.TextBox StatusNikah;
    private javax.swing.JTabbedPane TabRawat;
    private widget.Tanggal Tanggal;
    private widget.TextBox TanggalLahir;
    private widget.TextBox TempatLahir;
    private widget.Tanggal Tgl1;
    private widget.Tanggal Tgl2;
    private widget.TextBox URLSertisign;
    private javax.swing.JDialog WindowPhrase;
    private javax.swing.JDialog WindowURLSertisign;
    private javax.swing.ButtonGroup buttonGroup1;
    private widget.CekBox chkBerkasDigital;
    private widget.CekBox chkDiagnosaPenyakit;
    private widget.CekBox chkHasilPemeriksaanEKG;
    private widget.CekBox chkHasilPemeriksaanEcho;
    private widget.CekBox chkHasilPemeriksaanEchoPediatrik;
    private widget.CekBox chkHasilPemeriksaanEndoskopiFaringLaring;
    private widget.CekBox chkHasilPemeriksaanEndoskopiHidung;
    private widget.CekBox chkHasilPemeriksaanEndoskopiTelinga;
    private widget.CekBox chkHasilPemeriksaanOCT;
    private widget.CekBox chkHasilPemeriksaanSlitLamp;
    private widget.CekBox chkHasilPemeriksaanTreadmill;
    private widget.CekBox chkHasilPemeriksaanUSG;
    private widget.CekBox chkHasilPemeriksaanUSGGynecologi;
    private widget.CekBox chkHasilPemeriksaanUSGNeonatus;
    private widget.CekBox chkHasilPemeriksaanUSGUrologi;
    private widget.CekBox chkPemeriksaanLaborat;
    private widget.CekBox chkPemeriksaanRadiologi;
    private widget.CekBox chkProsedurTindakan;
    private widget.CekBox chkSemua;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame8;
    private widget.InternalFrame internalFrame9;
    private widget.Label jLabel39;
    private widget.Label jLabel40;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.Label label17;
    private widget.Label label18;
    private widget.Label label19;
    private widget.Label label20;
    private widget.Label label21;
    private widget.Label label22;
    private widget.Label label23;
    private widget.Label label24;
    private widget.Label label25;
    private widget.Label label26;
    private widget.Label label27;
    private widget.Label label28;
    private widget.Label label29;
    private widget.panelisi panelGlass5;
    private widget.panelisi panelisi5;
    private widget.panelisi panelisi6;
    // End of variables declaration//GEN-END:variables

    public void setNoRm(String norm,String nama) {
        NoRM.setText(norm);
        NmPasien.setText(nama);
        isPasien();
        BtnCari1ActionPerformed(null);
    }
    
    public void setNoRawat(String norawat) {
        TabRawat.setSelectedIndex(2);
        NoRawat.setText(norawat);
        R4.setSelected(true);
    }

    private void isPasien() {
        try{
            ps=koneksi.prepareStatement(
                    "select pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tmp_lahir,pasien.tgl_lahir,pasien.agama,"+
                    "bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,pasien.gol_darah,pasien.nm_ibu,pasien.stts_nikah,pasien.pnd, "+
                    "concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) as alamat,pasien.pekerjaan "+
                    "from pasien inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
                    "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik "+
                    "inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "+
                    "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "+
                    "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "+
                    "where pasien.no_rkm_medis=?");
            try {
                ps.setString(1,NoRM.getText());
                rs=ps.executeQuery();
                if(rs.next()){
                    NoRM.setText(rs.getString("no_rkm_medis"));
                    NmPasien.setText(rs.getString("nm_pasien"));
                    Jk.setText(rs.getString("jk"));
                    TempatLahir.setText(rs.getString("tmp_lahir"));
                    TanggalLahir.setText(rs.getString("tgl_lahir"));
                    Alamat.setText(rs.getString("alamat"));
                    GD.setText(rs.getString("gol_darah"));
                    IbuKandung.setText(rs.getString("nm_ibu"));
                    Agama.setText(rs.getString("agama"));
                    StatusNikah.setText(rs.getString("stts_nikah"));
                    Pendidikan.setText(rs.getString("pnd"));
                    Bahasa.setText(rs.getString("nama_bahasa"));
                    CacatFisik.setText(rs.getString("nama_cacat"));
                    Pekerjaan.setText(rs.getString("pekerjaan"));
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }
    }
        
    private void isMenu(){
        if(ChkAccor.isSelected()==true){
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(275,HEIGHT));
            FormMenu.setVisible(true); 
            ChkAccor.setVisible(true);
        }else if(ChkAccor.isSelected()==false){  
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(15,HEIGHT));
            FormMenu.setVisible(false);    
            ChkAccor.setVisible(true);
        }
    }

    private void tampilPerawatan() {
        try{   
            htmlContent = new StringBuilder();
            if(R1.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab,"+
                    "reg_periksa.umurdaftar,reg_periksa.sttsumur "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? order by reg_periksa.tgl_registrasi desc limit 5");
            }else if(R2.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab,"+
                    "reg_periksa.umurdaftar,reg_periksa.sttsumur "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? order by reg_periksa.tgl_registrasi");
            }else if(R3.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab,"+
                    "reg_periksa.umurdaftar,reg_periksa.sttsumur "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? and "+
                    "reg_periksa.tgl_registrasi between ? and ? order by reg_periksa.tgl_registrasi");
            }else if(R4.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab,"+
                    "reg_periksa.umurdaftar,reg_periksa.sttsumur "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? and reg_periksa.no_rawat=?");
            }
            
            try {
                i=0;
                if(R1.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                }else if(R2.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                }else if(R3.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                    ps.setString(2,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                    ps.setString(3,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                }else if(R4.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                    ps.setString(2,NoRawat.getText().trim());
                }            
                urut=1;
                rs=ps.executeQuery();
                while(rs.next()){
                    ps2=koneksi.prepareStatement(
                        "select poliklinik.nm_poli,dokter.nm_dokter from rujukan_internal_poli "+
                        "inner join poliklinik on rujukan_internal_poli.kd_poli=poliklinik.kd_poli "+
                        "inner join dokter on rujukan_internal_poli.kd_dokter=dokter.kd_dokter "+
                        "where no_rawat=?"
                    );
                    try {
                        dokterrujukan="";
                        polirujukan="";
                        ps2.setString(1,rs.getString("no_rawat"));
                        rs2=ps2.executeQuery();
                        while(rs2.next()){
                            polirujukan=polirujukan+", "+rs2.getString("nm_poli");
                            dokterrujukan=dokterrujukan+", "+rs2.getString("nm_dokter");
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : "+e);
                    } finally{
                        if(rs2!=null){
                            rs2.close();
                        }
                        if(ps2!=null){
                            ps2.close();
                        }
                    }   

                    htmlContent.append(
                      "<tr class='isi'>").append( 
                        "<td valign='top' width='2%'>").append(urut).append("</td>").append(
                        "<td valign='top' width='18%'>No.Rawat</td>").append(
                        "<td valign='top' width='1%' align='center'>:</td>").append(
                        "<td valign='top' width='79%'>").append(rs.getString("no_rawat")).append("</td>").append(
                      "</tr>").append(
                      "<tr class='isi'>").append( 
                        "<td valign='top' width='2%'></td>").append(
                        "<td valign='top' width='18%'>No.Registrasi</td>").append(
                        "<td valign='top' width='1%' align='center'>:</td>").append(
                        "<td valign='top' width='79%'>").append(rs.getString("no_reg")).append("</td>").append(
                      "</tr>").append(
                      "<tr class='isi'>").append( 
                        "<td valign='top' width='2%'></td>").append(
                        "<td valign='top' width='18%'>Tanggal Registrasi</td>").append(
                        "<td valign='top' width='1%' align='center'>:</td>").append(
                        "<td valign='top' width='79%'>").append(rs.getString("tgl_registrasi")).append(" ").append(rs.getString("jam_reg")).append("</td>").append(
                      "</tr>").append(
                      "<tr class='isi'>").append( 
                        "<td valign='top' width='2%'></td>").append(
                        "<td valign='top' width='18%'>Umur Saat Daftar</td>").append(
                        "<td valign='top' width='1%' align='center'>:</td>").append(
                        "<td valign='top' width='79%'>").append(rs.getString("umurdaftar")).append(" ").append(rs.getString("sttsumur")).append("</td>").append(
                      "</tr>").append(
                      "<tr class='isi'>").append( 
                        "<td valign='top' width='2%'></td>").append(
                        "<td valign='top' width='18%'>Unit/Poliklinik</td>").append(
                        "<td valign='top' width='1%' align='center'>:</td>").append(
                        "<td valign='top' width='79%'>").append(rs.getString("nm_poli")).append(polirujukan).append("</td>").append(
                      "</tr>").append(
                      "<tr class='isi'>").append( 
                        "<td valign='top' width='2%'></td>").append(        
                        "<td valign='top' width='18%'>Dokter Poli</td>").append(
                        "<td valign='top' width='1%' align='center'>:</td>").append(
                        "<td valign='top' width='79%'>").append(rs.getString("nm_dokter")).append(dokterrujukan).append("</td>").append(
                      "</tr>"
                    );
                    if(rs.getString("status_lanjut").equals("Ranap")){
                        try{
                            rs3=koneksi.prepareStatement(
                                "select dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat='"+rs.getString("no_rawat")+"'").executeQuery();
                            if(rs3.next()){
                                htmlContent.append(
                                  "<tr class='isi'>").append( 
                                    "<td valign='top' width='2%'></td>").append(        
                                    "<td valign='top' width='18%'>DPJP Ranap</td>").append(
                                    "<td valign='top' width='1%' align='center'>:</td>").append(
                                    "<td valign='top' width='79%'>"
                                );
                                urutdpjp=1;
                                do{
                                    htmlContent.append(urutdpjp).append(". ").append(rs3.getString("nm_dokter")).append("&nbsp;&nbsp;");
                                    urutdpjp++;
                                }while(rs3.next());
                                htmlContent.append("</td>"+
                                  "</tr>"
                                );    
                            }
                        } catch (Exception e) {
                            System.out.println("Status Lanjut : "+e);
                        } finally{
                            if(rs3!=null){
                                rs3.close();
                            }
                        }
                    }
                    htmlContent.append( 
                      "<tr class='isi'>").append( 
                        "<td valign='top' width='2%'></td>").append(
                        "<td valign='top' width='18%'>Cara Bayar</td>").append(
                        "<td valign='top' width='1%' align='center'>:</td>").append(
                        "<td valign='top' width='79%'>").append(rs.getString("png_jawab")).append("</td>").append(
                      "</tr>").append(
                      "<tr class='isi'>").append( 
                        "<td valign='top' width='2%'></td>").append(        
                        "<td valign='top' width='18%'>Penanggung Jawab</td>").append(
                        "<td valign='top' width='1%' align='center'>:</td>").append(
                        "<td valign='top' width='79%'>").append(rs.getString("p_jawab")).append("</td>").append(
                      "</tr>").append(
                      "<tr class='isi'>").append( 
                        "<td valign='top' width='2%'></td>").append(         
                        "<td valign='top' width='18%'>Alamat P.J.</td>").append(
                        "<td valign='top' width='1%' align='center'>:</td>").append(
                        "<td valign='top' width='79%'>").append(rs.getString("almt_pj")).append("</td>").append(
                      "</tr>").append(
                      "<tr class='isi'>").append( 
                        "<td valign='top' width='2%'></td>").append(        
                        "<td valign='top' width='18%'>Hubungan P.J.</td>").append(
                        "<td valign='top' width='1%' align='center'>:</td>").append(
                        "<td valign='top' width='79%'>").append(rs.getString("hubunganpj")).append("</td>").append(
                      "</tr>").append(
                      "<tr class='isi'>").append( 
                        "<td valign='top' width='2%'></td>").append(        
                        "<td valign='top' width='18%'>Status</td>").append(
                        "<td valign='top' width='1%' align='center'>:</td>").append(
                        "<td valign='top' width='79%'>").append(rs.getString("status_lanjut")).append("</td>").append(
                      "</tr>"
                    );                            
                    urut++;
                    
                    menampilkanHasilPemeriksaanUSG(rs.getString("no_rawat"));
                    menampilkanHasilPemeriksaanUSGUrologi(rs.getString("no_rawat"));
                    menampilkanHasilPemeriksaanUSGNeonatus(rs.getString("no_rawat"));
                    menampilkanHasilPemeriksaanUSGGynocologi(rs.getString("no_rawat"));
                    menampilkanHasilPemeriksaanEKG(rs.getString("no_rawat"));
                    menampilkanHasilPemeriksaanSlitLamp(rs.getString("no_rawat"));
                    menampilkanHasilPemeriksaanOCT(rs.getString("no_rawat"));
                    menampilkanHasilPemeriksaanECHO(rs.getString("no_rawat"));
                    menampilkanHasilPemeriksaanECHOPediatrik(rs.getString("no_rawat"));
                    menampilkanHasilPemeriksaanTreadmill(rs.getString("no_rawat"));
                    menampilkanHasilEndoskopiFaringLaring(rs.getString("no_rawat"));
                    menampilkanHasilEndoskopiHidung(rs.getString("no_rawat"));
                    menampilkanHasilEndoskopiTelinga(rs.getString("no_rawat"));
                    menampilkanDiagnosa(rs.getString("no_rawat"));
                    menampilkanBerkasDigital(rs.getString("no_rawat"));
                    
                    biayaperawatan=rs.getDouble("biaya_reg");
                    //biaya administrasi
                    htmlContent.append(
                       "<tr class='isi'>").append( 
                         "<td valign='top' width='2%'></td>").append(        
                         "<td valign='top' width='18%'>Biaya & Perawatan</td>").append(
                         "<td valign='top' width='1%' align='center'>:</td>").append(
                         "<td valign='top' width='79%'>").append(
                             "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>").append(
                               "<tr>").append(
                                 "<td valign='top' width='89%'>Administrasi</td>").append(
                                 "<td valign='top' width='1%' align='right'>:</td>").append(
                                 "<td valign='top' width='10%' align='right'>").append(Valid.SetAngka(rs.getDouble("biaya_reg"))).append("</td>").append(
                               "</tr>").append(
                             "</table>"
                    );
                    
                    //menampilkan pemeriksaan radiologi
                    if(chkPemeriksaanRadiologi.isSelected()==true){
                        try{
                            rs2=koneksi.prepareStatement(
                                 "select periksa_radiologi.tgl_periksa,periksa_radiologi.jam,periksa_radiologi.kd_jenis_prw, "+
                                 "jns_perawatan_radiologi.nm_perawatan,petugas.nama,periksa_radiologi.biaya,periksa_radiologi.dokter_perujuk,"+
                                 "dokter.nm_dokter,concat("+
                                 "if(periksa_radiologi.proyeksi<>'',concat('Proyeksi : ',periksa_radiologi.proyeksi,', '),''),"+
                                 "if(periksa_radiologi.kV<>'',concat('kV : ',periksa_radiologi.kV,', '),''),"+
                                 "if(periksa_radiologi.mAS<>'',concat('mAS : ',periksa_radiologi.mAS,', '),''),"+
                                 "if(periksa_radiologi.FFD<>'',concat('FFD : ',periksa_radiologi.FFD,', '),''),"+
                                 "if(periksa_radiologi.BSF<>'',concat('BSF : ',periksa_radiologi.BSF,', '),''),"+
                                 "if(periksa_radiologi.inak<>'',concat('Inak : ',periksa_radiologi.inak,', '),''),"+
                                 "if(periksa_radiologi.jml_penyinaran<>'',concat('Jml Penyinaran : ',periksa_radiologi.jml_penyinaran,', '),''),"+
                                 "if(periksa_radiologi.dosis<>'',concat('Dosis Radiasi : ',periksa_radiologi.dosis),'')) as proyeksi "+
                                 "from periksa_radiologi inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                                 "inner join petugas on periksa_radiologi.nip=petugas.nip inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                                 "where periksa_radiologi.no_rawat='"+rs.getString("no_rawat")+"' order by periksa_radiologi.tgl_periksa,periksa_radiologi.jam").executeQuery();
                            if(rs2.next()){                                    
                                htmlContent.append(  
                                  "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>").append(
                                    "<tr><td valign='top' colspan='5'>Pemeriksaan Radiologi</td><td valign='top' colspan='1' align='right'>:</td><td valign='top'></td></tr>").append(            
                                    "<tr align='center'>").append(
                                      "<td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td>").append(
                                      "<td valign='top' width='15%' bgcolor='#FFFAF8'>Tanggal</td>").append(
                                      "<td valign='top' width='10%' bgcolor='#FFFAF8'>Kode</td>").append(
                                      "<td valign='top' width='26%' bgcolor='#FFFAF8'>Nama Pemeriksaan</td>").append(
                                      "<td valign='top' width='18%' bgcolor='#FFFAF8'>Dokter PJ</td>").append(
                                      "<td valign='top' width='17%' bgcolor='#FFFAF8'>Petugas</td>").append(
                                      "<td valign='top' width='10%' bgcolor='#FFFAF8'>Biaya</td>").append(
                                    "</tr>");
                                w=1;
                                do{
                                    htmlContent.append(
                                         "<tr>").append(
                                            "<td valign='top' align='center'>").append(w).append("</td>").append(
                                            "<td valign='top'>").append(rs2.getString("tgl_periksa")).append(" ").append(rs2.getString("jam")).append("</td>").append(
                                            "<td valign='top'>").append(rs2.getString("kd_jenis_prw")).append("</td>").append(
                                            "<td valign='top'>").append(rs2.getString("nm_perawatan")).append("<br>").append(rs2.getString("proyeksi")).append("</td>").append(
                                            "<td valign='top'>").append(rs2.getString("nm_dokter")).append("</td>").append(
                                            "<td valign='top'>").append(rs2.getString("nama")).append("</td>").append(
                                            "<td valign='top' align='right'>").append(Valid.SetAngka(rs2.getDouble("biaya"))).append("</td>").append(
                                         "</tr>"); 
                                    w++;
                                    biayaperawatan=biayaperawatan+rs2.getDouble("biaya");
                                }while(rs2.next());
                                htmlContent.append(
                                  "</table>");
                            }                                
                        } catch (Exception e) {
                            System.out.println("Notifikasi : "+e);
                        } finally{
                            if(rs2!=null){
                                rs2.close();
                            }
                        }

                        //hasil pemeriksaan radiologi
                        try{
                            rs2=koneksi.prepareStatement(
                                 "select hasil_radiologi.tgl_periksa,hasil_radiologi.jam,hasil_radiologi.hasil from hasil_radiologi where hasil_radiologi.no_rawat='"+rs.getString("no_rawat")+"' order by hasil_radiologi.tgl_periksa,hasil_radiologi.jam").executeQuery();
                            if(rs2.next()){                                    
                                htmlContent.append(  
                                  "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>").append(
                                    "<tr><td valign='top' colspan='3'>Bacaan/Hasil Radiologi</td></tr>").append(  
                                    "<tr align='center'>").append(
                                      "<td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td>").append(
                                      "<td valign='top' width='15%' bgcolor='#FFFAF8'>Tanggal</td>").append(
                                      "<td valign='top' width='81%' bgcolor='#FFFAF8'>Hasil Pemeriksaan</td>").append(
                                    "</tr>");
                                w=1;
                                do{
                                    htmlContent.append(
                                         "<tr>").append(
                                            "<td valign='top' align='center'>").append(w).append("</td>").append(
                                            "<td valign='top'>").append(rs2.getString("tgl_periksa")).append(" ").append(rs2.getString("jam")).append("</td>").append(
                                            "<td valign='top'>").append(rs2.getString("hasil").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                         "</tr>"); 
                                    w++;
                                }while(rs2.next());
                                htmlContent.append(
                                  "</table>");
                            }                                
                        } catch (Exception e) {
                            System.out.println("Notifikasi : "+e);
                        } finally{
                            if(rs2!=null){
                                rs2.close();
                            }
                        }

                        //gambar pemeriksaan radiologi
                        try{
                            rs2=koneksi.prepareStatement(
                                 "select gambar_radiologi.tgl_periksa,gambar_radiologi.jam,gambar_radiologi.lokasi_gambar from gambar_radiologi where gambar_radiologi.no_rawat='"+rs.getString("no_rawat")+"' order by gambar_radiologi.tgl_periksa,gambar_radiologi.jam").executeQuery();
                            if(rs2.next()){                                    
                                htmlContent.append(  
                                  "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>").append(
                                    "<tr><td valign='top' colspan='3'>Gambar Radiologi</td></tr>").append(  
                                    "<tr align='center'>").append(
                                      "<td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td>").append(
                                      "<td valign='top' width='15%' bgcolor='#FFFAF8'>Tanggal</td>").append(
                                      "<td valign='top' width='81%' bgcolor='#FFFAF8'>Gambar Radiologi</td>").append(
                                    "</tr>");
                                w=1;
                                do{
                                    htmlContent.append(
                                         "<tr height='").append(internalFrame2.getHeight()).append("px'>").append(
                                            "<td valign='top' align='center'>").append(w).append("</td>").append(
                                            "<td valign='top'>").append(rs2.getString("tgl_periksa")).append(" ").append(rs2.getString("jam")).append("</td>").append(
                                            "<td valign='top' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/radiologi/").append(rs2.getString("lokasi_gambar")).append("'><img alt='Gambar Radiologi' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/radiologi/").append(rs2.getString("lokasi_gambar")).append("' width='450' height='450'/></a></td>").append(
                                         "</tr>"); 
                                    w++;
                                }while(rs2.next());
                                htmlContent.append(
                                  "</table>");
                            }                                
                        } catch (Exception e) {
                            System.out.println("Notifikasi : "+e);
                        } finally{
                            if(rs2!=null){
                                rs2.close();
                            }
                        }
                    }
                    
                    //menampilkan pemeriksaan laborat
                    if(chkPemeriksaanLaborat.isSelected()==true){
                        try {
                            rs4=koneksi.prepareStatement(
                                 "select periksa_lab.tgl_periksa,periksa_lab.jam from periksa_lab where periksa_lab.kategori<>'PA' and periksa_lab.no_rawat='"+rs.getString("no_rawat")+"' "+
                                 "group by concat(periksa_lab.no_rawat,periksa_lab.tgl_periksa,periksa_lab.jam) order by periksa_lab.tgl_periksa,periksa_lab.jam").executeQuery();
                            if(rs4.next()){
                                htmlContent.append(  
                                  "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>").append(
                                    "<tr><td valign='top' colspan='5'>Pemeriksaan Laboratorium PK & MB</td><td valign='top' colspan='1' align='right'>:</td><td valign='top'></td></tr>").append(            
                                    "<tr align='center'>").append(
                                      "<td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td>").append(
                                      "<td valign='top' width='15%' bgcolor='#FFFAF8'>Tanggal</td>").append(
                                      "<td valign='top' width='10%' bgcolor='#FFFAF8'>Kode</td>").append(
                                      "<td valign='top' width='26%' bgcolor='#FFFAF8'>Nama Pemeriksaan</td>").append(
                                      "<td valign='top' width='18%' bgcolor='#FFFAF8'>Dokter PJ</td>").append(
                                      "<td valign='top' width='17%' bgcolor='#FFFAF8'>Petugas</td>").append(
                                      "<td valign='top' width='10%' bgcolor='#FFFAF8'>Biaya</td>").append(
                                    "</tr>");
                                rs4.beforeFirst();
                                w=1;
                                while(rs4.next()){
                                    try{
                                        rs2=koneksi.prepareStatement(
                                             "select periksa_lab.kd_jenis_prw, "+
                                             "jns_perawatan_lab.nm_perawatan,petugas.nama,periksa_lab.biaya,periksa_lab.dokter_perujuk,dokter.nm_dokter "+
                                             "from periksa_lab inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                                             "inner join petugas on periksa_lab.nip=petugas.nip inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                                             "where periksa_lab.kategori<>'PA' and periksa_lab.no_rawat='"+rs.getString("no_rawat")+"' "+
                                             "and periksa_lab.tgl_periksa='"+rs4.getString("tgl_periksa")+"' and periksa_lab.jam='"+rs4.getString("jam")+"'").executeQuery();
                                        s=1;
                                        while(rs2.next()){
                                            if(s==1){
                                                htmlContent.append(
                                                    "<tr>").append(
                                                       "<td valign='top' align='center'>").append(w).append("</td>").append(
                                                       "<td valign='top'>").append(rs4.getString("tgl_periksa")).append(" ").append(rs4.getString("jam")).append("</td>").append(
                                                       "<td valign='top'>").append(rs2.getString("kd_jenis_prw")).append("</td>").append(
                                                       "<td valign='top'>").append(rs2.getString("nm_perawatan")).append("</td>").append(
                                                       "<td valign='top'>").append(rs2.getString("nm_dokter")).append("</td>").append(
                                                       "<td valign='top'>").append(rs2.getString("nama")).append("</td>").append(
                                                       "<td valign='top' align='right'>").append(Valid.SetAngka(rs2.getDouble("biaya"))).append("</td>").append(
                                                    "</tr>"
                                               ); 
                                            }else{
                                                htmlContent.append(
                                                     "<tr>").append(
                                                        "<td valign='top' align='center'></td>").append(
                                                        "<td valign='top'></td>").append(
                                                        "<td valign='top'>").append(rs2.getString("kd_jenis_prw")).append("</td>").append(
                                                        "<td valign='top'>").append(rs2.getString("nm_perawatan")).append("</td>").append(
                                                        "<td valign='top'>").append(rs2.getString("nm_dokter")).append("</td>").append(
                                                        "<td valign='top'>").append(rs2.getString("nama")).append("</td>").append(
                                                        "<td valign='top' align='right'>").append(Valid.SetAngka(rs2.getDouble("biaya"))).append("</td>").append(
                                                     "</tr>"
                                                ); 
                                            }
                                                
                                            biayaperawatan=biayaperawatan+rs2.getDouble("biaya");
                                            
                                            try {
                                                rs3=koneksi.prepareStatement(
                                                    "select template_laboratorium.Pemeriksaan, detail_periksa_lab.nilai,"+
                                                    "template_laboratorium.satuan,detail_periksa_lab.nilai_rujukan,detail_periksa_lab.biaya_item,"+
                                                    "detail_periksa_lab.keterangan from detail_periksa_lab inner join "+
                                                    "template_laboratorium on detail_periksa_lab.id_template=template_laboratorium.id_template "+
                                                    "where detail_periksa_lab.no_rawat='"+rs.getString("no_rawat")+"' and "+
                                                    "detail_periksa_lab.kd_jenis_prw='"+rs2.getString("kd_jenis_prw")+"' and "+
                                                    "detail_periksa_lab.tgl_periksa='"+rs4.getString("tgl_periksa")+"' and "+
                                                    "detail_periksa_lab.jam='"+rs4.getString("jam")+"' order by detail_periksa_lab.kd_jenis_prw,template_laboratorium.urut ").executeQuery();
                                                if(rs3.next()){ 
                                                    htmlContent.append(
                                                        "<tr>").append(
                                                           "<td valign='top' align='center'></td>").append(
                                                           "<td valign='top'></td>").append(
                                                           "<td valign='top'></td>").append(
                                                           "<td valign='top' align='center' bgcolor='#FFFAF8'>Detail Pemeriksaan</td>").append(
                                                           "<td valign='top' align='center' bgcolor='#FFFAF8'>Hasil</td>").append(
                                                           "<td valign='top' align='center' bgcolor='#FFFAF8'>Nilai Rujukan</td>").append(
                                                           "<td valign='top' align='right'></td>").append(
                                                        "</tr>");
                                                    do{
                                                        switch (rs3.getString("keterangan").toLowerCase()) {
                                                            case "l":
                                                                htmlContent.append(
                                                                    "<tr>").append(
                                                                       "<td valign='top' align='center'></td>").append(
                                                                       "<td valign='top'></td>").append(
                                                                       "<td valign='top'></td>").append(
                                                                       "<td valign='top'>").append(rs3.getString("Pemeriksaan")).append("</td>").append(
                                                                       "<td valign='top' style='color:#0000FF'>").append(rs3.getString("nilai").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append(" ").append(rs3.getString("satuan")).append("</td>").append(
                                                                       "<td valign='top'>").append(rs3.getString("nilai_rujukan").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                                                       "<td valign='top' align='right'>").append(Valid.SetAngka(rs3.getDouble("biaya_item"))).append("</td>").append(
                                                                    "</tr>"); 
                                                                break;
                                                            case "h":
                                                                htmlContent.append(
                                                                    "<tr>").append(
                                                                       "<td valign='top' align='center'></td>").append(
                                                                       "<td valign='top'></td>").append(
                                                                       "<td valign='top'></td>").append(
                                                                       "<td valign='top'>").append(rs3.getString("Pemeriksaan")).append("</td>").append(
                                                                       "<td valign='top' style='color:#FF0000'>").append(rs3.getString("nilai").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append(" ").append(rs3.getString("satuan")).append("</td>").append(
                                                                       "<td valign='top'>").append(rs3.getString("nilai_rujukan").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                                                       "<td valign='top' align='right'>").append(Valid.SetAngka(rs3.getDouble("biaya_item"))).append("</td>").append(
                                                                    "</tr>"); 
                                                                break;
                                                            case "t":
                                                                htmlContent.append(
                                                                    "<tr>").append(
                                                                       "<td valign='top' align='center'></td>").append(
                                                                       "<td valign='top'></td>").append(
                                                                       "<td valign='top'></td>").append(
                                                                       "<td valign='top'>").append(rs3.getString("Pemeriksaan")).append("</td>").append(
                                                                       "<td valign='top'><b>").append(rs3.getString("nilai").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append(" ").append(rs3.getString("satuan")).append("</b></td>").append(
                                                                       "<td valign='top'>").append(rs3.getString("nilai_rujukan").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                                                       "<td valign='top' align='right'>").append(Valid.SetAngka(rs3.getDouble("biaya_item"))).append("</td>").append(
                                                                    "</tr>"); 
                                                                break;
                                                            default:
                                                                htmlContent.append(
                                                                    "<tr>").append(
                                                                       "<td valign='top' align='center'></td>").append(
                                                                       "<td valign='top'></td>").append(
                                                                       "<td valign='top'></td>").append(
                                                                       "<td valign='top'>").append(rs3.getString("Pemeriksaan")).append("</td>").append(
                                                                       "<td valign='top'>").append(rs3.getString("nilai").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append(" ").append(rs3.getString("satuan")).append("</td>").append(
                                                                       "<td valign='top'>").append(rs3.getString("nilai_rujukan").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                                                       "<td valign='top' align='right'>").append(Valid.SetAngka(rs3.getDouble("biaya_item"))).append("</td>").append(
                                                                    "</tr>"); 
                                                        }
                                                        
                                                        biayaperawatan=biayaperawatan+rs3.getDouble("biaya_item");
                                                    }while(rs3.next());                              
                                                }
                                            } catch (Exception e) {
                                                System.out.println("Notifikasi : "+e);
                                            } finally{
                                                if(rs3!=null){
                                                    rs3.close();
                                                }
                                            }
                                            s++;
                                        }

                                        try {
                                            rs3=koneksi.prepareStatement("select saran_kesan_lab.saran,saran_kesan_lab.kesan from saran_kesan_lab where saran_kesan_lab.no_rawat='"+rs.getString("no_rawat")+"' and saran_kesan_lab.tgl_periksa='"+rs4.getString("tgl_periksa")+"' and saran_kesan_lab.jam='"+rs4.getString("jam")+"'").executeQuery();
                                            if(rs3.next()){      
                                                htmlContent.append(
                                                        "<tr>").append(
                                                           "<td valign='top' align='center'></td>").append(
                                                           "<td valign='top'>Kesan</td>").append(
                                                           "<td valign='top' colspan='5'>: ").append(rs3.getString("kesan")).append("</td>").append(
                                                        "</tr>").append(
                                                        "<tr>").append(
                                                           "<td valign='top' align='center'></td>").append(
                                                           "<td valign='top'>Saran</td>").append(
                                                           "<td valign='top' colspan='5'>: ").append(rs3.getString("saran")).append("</td>").append(
                                                        "</tr>");
                                            } 
                                        } catch (Exception e) {
                                            System.out.println("Notif : "+e);
                                        } finally{
                                            if(rs3!=null){
                                                rs3.close();
                                            }
                                        }   
                                        w++;
                                    } catch (Exception e) {
                                        System.out.println("Notifikasi Lab : "+e);
                                    } finally{
                                        if(rs2!=null){
                                            rs2.close();
                                        }
                                    }
                                }
                                htmlContent.append(
                                              "</table>");
                            }
                        } catch (Exception e) {
                            System.out.println("Notif : "+e);
                        } finally{
                            if(rs4!=null){
                                rs4.close();
                            }
                        }

                        try{
                            rs2=koneksi.prepareStatement(
                                 "select periksa_lab.tgl_periksa,periksa_lab.jam,periksa_lab.kd_jenis_prw, "+
                                 "jns_perawatan_lab.nm_perawatan,petugas.nama,periksa_lab.biaya,periksa_lab.dokter_perujuk,dokter.nm_dokter "+
                                 "from periksa_lab inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                                 "inner join petugas on periksa_lab.nip=petugas.nip inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                                 "where periksa_lab.kategori='PA' and periksa_lab.no_rawat='"+rs.getString("no_rawat")+"' order by periksa_lab.tgl_periksa,periksa_lab.jam").executeQuery();
                            if(rs2.next()){
                                htmlContent.append(  
                                  "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>").append(
                                    "<tr><td valign='top' colspan='5'>Pemeriksaan Laboratorium PA</td><td valign='top' colspan='1' align='right'>:</td><td valign='top'></td></tr>").append(            
                                    "<tr align='center'>").append(
                                      "<td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td>").append(
                                      "<td valign='top' width='15%' bgcolor='#FFFAF8'>Tanggal</td>").append(
                                      "<td valign='top' width='10%' bgcolor='#FFFAF8'>Kode</td>").append(
                                      "<td valign='top' width='26%' bgcolor='#FFFAF8'>Nama Pemeriksaan</td>").append(
                                      "<td valign='top' width='18%' bgcolor='#FFFAF8'>Dokter PJ</td>").append(
                                      "<td valign='top' width='17%' bgcolor='#FFFAF8'>Petugas</td>").append(
                                      "<td valign='top' width='10%' bgcolor='#FFFAF8'>Biaya</td>").append(
                                    "</tr>");
                                w=1;
                                do{
                                    htmlContent.append(
                                         "<tr>").append(
                                            "<td valign='top' align='center'>").append(w).append("</td>").append(
                                            "<td valign='top'>").append(rs2.getString("tgl_periksa")).append(" ").append(rs2.getString("jam")).append("</td>").append(
                                            "<td valign='top'>").append(rs2.getString("kd_jenis_prw")).append("</td>").append(
                                            "<td valign='top'>").append(rs2.getString("nm_perawatan")).append("</td>").append(
                                            "<td valign='top'>").append(rs2.getString("nm_dokter")).append("</td>").append(
                                            "<td valign='top'>").append(rs2.getString("nama")).append("</td>").append(
                                            "<td valign='top' align='right'>").append(Valid.SetAngka(rs2.getDouble("biaya"))).append("</td>").append(
                                         "</tr>"
                                    ); 
                                    biayaperawatan=biayaperawatan+rs2.getDouble("biaya");
                                    try {
                                        rs3=koneksi.prepareStatement(
                                            "select detail_periksa_labpa.diagnosa_klinik,detail_periksa_labpa.makroskopik,detail_periksa_labpa.mikroskopik,detail_periksa_labpa.kesimpulan,detail_periksa_labpa.kesan from detail_periksa_labpa "+
                                            "where detail_periksa_labpa.no_rawat='"+rs.getString("no_rawat")+"' and detail_periksa_labpa.kd_jenis_prw='"+rs2.getString("kd_jenis_prw")+"' and "+
                                            "detail_periksa_labpa.tgl_periksa='"+rs2.getString("tgl_periksa")+"' and detail_periksa_labpa.jam='"+rs2.getString("jam")+"'").executeQuery();
                                        if(rs3.next()){ 
                                            file=Sequel.cariIsi("select detail_periksa_labpa_gambar.photo from detail_periksa_labpa_gambar where detail_periksa_labpa_gambar.no_rawat='"+rs.getString("no_rawat")+"' and detail_periksa_labpa_gambar.kd_jenis_prw='"+rs2.getString("kd_jenis_prw")+"' and detail_periksa_labpa_gambar.tgl_periksa='"+rs2.getString("tgl_periksa")+"' and detail_periksa_labpa_gambar.jam='"+rs2.getString("jam")+"'");
                                            htmlContent.append(
                                                "<tr>").append(
                                                   "<td valign='top' align='center'></td>").append(
                                                   "<td valign='top'></td>").append(
                                                   "<td valign='top'>Diagnosa Klinis</td>").append(
                                                   "<td valign='top' colspan='4'>: ").append(rs3.getString("diagnosa_klinik")).append("</td>").append(
                                                "</tr>").append(
                                                "<tr>").append(
                                                   "<td valign='top' align='center'></td>").append(
                                                   "<td valign='top'></td>").append(
                                                   "<td valign='top'>Makroskopik</td>").append(
                                                   "<td valign='top' colspan='4'>: ").append(rs3.getString("makroskopik")).append("</td>").append(
                                                "</tr>").append(
                                                "<tr>").append(
                                                   "<td valign='top' align='center'></td>").append(
                                                   "<td valign='top'></td>").append(
                                                   "<td valign='top'>Mikroskopik</td>").append(
                                                   "<td valign='top' colspan='4'>: ").append(rs3.getString("mikroskopik")).append("</td>").append(
                                                "</tr>").append(
                                                "<tr>").append(
                                                   "<td valign='top' align='center'></td>").append(
                                                   "<td valign='top'></td>").append(
                                                   "<td valign='top'>Kesimpulan</td>").append(
                                                   "<td valign='top' colspan='4'>: ").append(rs3.getString("kesimpulan")).append("</td>").append(
                                                "</tr>").append(
                                                "<tr>").append(
                                                   "<td valign='top' align='center'></td>").append(
                                                   "<td valign='top'></td>").append(
                                                   "<td valign='top'>Kesan</td>").append(
                                                   "<td valign='top' colspan='4'>: ").append(rs3.getString("kesan")).append("</td>").append(
                                                "</tr>"
                                            );  
                                            if(!file.equals("")){
                                                htmlContent.append(
                                                    "<tr>").append(
                                                        "<td valign='top' align='center'></td>").append(
                                                        "<td valign='top'></td>").append(
                                                        "<td valign='top' colspan='5' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/labpa/").append(file).append("'><img alt='Gambar PA' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/labpa/").append(file).append("' width='450' height='450'/></a></td>").append(
                                                    "</tr>"
                                                );
                                            }
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Notifikasi : "+e);
                                    } finally{
                                        if(rs3!=null){
                                            rs3.close();
                                        }
                                    }
                                    w++;
                                }while(rs2.next());

                                htmlContent.append(
                                  "</table>");
                            }                                
                        } catch (Exception e) {
                            System.out.println("Notifikasi Lab : "+e);
                        } finally{
                            if(rs2!=null){
                                rs2.close();
                            }
                        }
                    }
                    
                    htmlContent.append(
                        "<tr class='isi'><td></td><td colspan='3' align='right'>&nbsp;</tr>"
                    );
                }
                
                LoadHTMLRiwayatPerawatan.setText(
                    "<html>"+
                      "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                       htmlContent.toString()+
                      "</table>"+
                    "</html>");
                htmlContent=null;
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }                
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }

    private void panggilLaporan(String teks) {
        try{
            File g = new File("file.css");            
            BufferedWriter bg = new BufferedWriter(new FileWriter(g));
            bg.write(".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}.isi a{text-decoration:none;color:#8b9b95;padding:0 0 0 0px;font-family: Tahoma;font-size: 8.5px;border: white;}");
            bg.close();

            File f = new File("riwayat.html");            
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(
                 teks.replaceAll("<head>","<head><link href=\"file.css\" rel=\"stylesheet\" type=\"text/css\" />").
                      replaceAll("<body>",
                                 "<body>"+
                                    "<table width='100%' align='center' border='0' class='tbl_form' cellspacing='0' cellpadding='0'>" +
                                        "<tr>" +
                                            "<td width='15%' border='0'>" +
                                                "<img width='50' height='50' src='data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(Sequel.cariGambar("select setting.logo from setting").readAllBytes())+"'/>" +
                                            "</td>" +
                                            "<td width='85%' border='0'>" +
                                                "<center>" +
                                                    "<font color='000000' size='3'  face='Tahoma'>"+akses.getnamars()+"</font><br>"+
                                                    "<font color='000000' size='1'  face='Tahoma'>"+
                                                        akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br/>" +
                                                        akses.getkontakrs()+", E-mail : "+akses.getemailrs()+
                                                        "<br>RIWAYAT PERAWATAN" +
                                                    "</font> " +
                                                "</center>" +
                                            "</td>" +
                                        "</tr>" +
                                    "</table><br>"+
                                    "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>No.RM</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+NoRM.getText().trim()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Nama Pasien</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+NmPasien.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Alamat</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+Alamat.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Jenis Kelamin</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+Jk.getText().replaceAll("L","Laki-Laki").replaceAll("P","Perempuan")+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Tempat & Tanggal Lahir</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+TempatLahir.getText()+" "+TanggalLahir.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Ibu Kandung</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+IbuKandung.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Golongan Darah</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+GD.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Status Nikah</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+StatusNikah.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Agama</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+Agama.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Pendidikan Terakhir</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+Pendidikan.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Bahasa Dipakai</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+Bahasa.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Cacat Fisik</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+CacatFisik.getText()+"</td>"+
                                       "</tr>"+
                                    "</table>"            
                      ).
                      replaceAll((getClass().getResource("/picture/"))+"","./gambar/")
            );  
            bw.close();
            Desktop.getDesktop().browse(f.toURI());
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }   
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,126));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }

        private void menampilkanDiagnosa(String norawat) {
        try{
            if(chkDiagnosaPenyakit.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                            "select diagnosa_pasien.kd_penyakit,penyakit.nm_penyakit, diagnosa_pasien.status "+
                            "from diagnosa_pasien inner join penyakit on diagnosa_pasien.kd_penyakit=penyakit.kd_penyakit "+
                            "where diagnosa_pasien.no_rawat='"+norawat+"' order by diagnosa_pasien.prioritas").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>").append( 
                            "<td valign='top' width='2%'></td>").append(        
                            "<td valign='top' width='18%'>Diagnosa/Penyakit/ICD 10</td>").append(
                            "<td valign='top' width='1%' align='center'>:</td>").append(
                            "<td valign='top' width='79%'>").append(
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>").append(
                                 "<tr align='center'><td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td><td valign='top' width='24%' bgcolor='#FFFAF8'>Kode</td><td valign='top' width='51%' bgcolor='#FFFAF8'>Nama Penyakit</td><td valign='top' width='23%' bgcolor='#FFFAF8'>Status</td></tr>"
                        );
                        w=1;
                        do{
                            htmlContent.append("<tr><td valign='top' align='center'>").append(w).append("</td><td valign='top'>").append(rs2.getString("kd_penyakit")).append("</td><td valign='top'>").append(rs2.getString("nm_penyakit")).append("</td><td valign='top'>").append(rs2.getString("status")).append("</td></tr>");                                        
                            w++;
                        }while(rs2.next());
                        htmlContent.append(
                              "</table>").append(
                            "</td>").append(
                          "</tr>");
                    }                                    
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }

            if(chkProsedurTindakan.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                            "select prosedur_pasien.kode,icd9.deskripsi_panjang, prosedur_pasien.status "+
                            "from prosedur_pasien inner join icd9 "+
                            "on prosedur_pasien.kode=icd9.kode "+
                            "where prosedur_pasien.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>").append( 
                            "<td valign='top' width='2%'></td>").append(        
                            "<td valign='top' width='18%'>Prosedur/Tindakan/ICD 9</td>").append(
                            "<td valign='top' width='1%' align='center'>:</td>").append(
                            "<td valign='top' width='79%'>").append(
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>").append(
                                 "<tr align='center'><td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td><td valign='top' width='24%' bgcolor='#FFFAF8'>Kode</td><td valign='top' width='51%' bgcolor='#FFFAF8'>Nama Prosedur</td><td valign='top' width='23%' bgcolor='#FFFAF8'>Status</td></tr>"
                        );
                        w=1;
                        do{
                            htmlContent.append("<tr><td valign='top' align='center'>").append(w).append("</td><td valign='top'>").append(rs2.getString("kode")).append("</td><td valign='top'>").append(rs2.getString("deskripsi_panjang")).append("</td><td valign='top'>").append(rs2.getString("status")).append("</td></tr>");                                        
                            w++;
                        }while(rs2.next());
                        htmlContent.append(
                              "</table>").append(
                            "</td>").append(
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Diagnosa : "+e);
        }
    }
          
    private void menampilkanHasilPemeriksaanUSG(String norawat) {
        try {
            if(chkHasilPemeriksaanUSG.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                            "select hasil_pemeriksaan_usg.kd_dokter,dokter.nm_dokter,hasil_pemeriksaan_usg.diagnosa_klinis,hasil_pemeriksaan_usg.kiriman_dari,"+
                            "hasil_pemeriksaan_usg.hta,hasil_pemeriksaan_usg.kantong_gestasi,hasil_pemeriksaan_usg.ukuran_bokongkepala,"+
                            "hasil_pemeriksaan_usg.jenis_prestasi,hasil_pemeriksaan_usg.diameter_biparietal,hasil_pemeriksaan_usg.panjang_femur,"+
                            "hasil_pemeriksaan_usg.lingkar_abdomen,hasil_pemeriksaan_usg.tafsiran_berat_janin,hasil_pemeriksaan_usg.usia_kehamilan,"+
                            "hasil_pemeriksaan_usg.plasenta_berimplatansi,hasil_pemeriksaan_usg.derajat_maturitas,hasil_pemeriksaan_usg.jumlah_air_ketuban,"+
                            "hasil_pemeriksaan_usg.indek_cairan_ketuban,hasil_pemeriksaan_usg.kelainan_kongenital,hasil_pemeriksaan_usg.peluang_sex,"+
                            "hasil_pemeriksaan_usg.kesimpulan,hasil_pemeriksaan_usg.tanggal "+
                            "from hasil_pemeriksaan_usg inner join dokter on hasil_pemeriksaan_usg.kd_dokter=dokter.kd_dokter "+
                            "where hasil_pemeriksaan_usg.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>").append( 
                            "<td valign='top' width='2%'></td>").append(        
                            "<td valign='top' width='18%'>Hasil USG Kandungan</td>").append(
                            "<td valign='top' width='1%' align='center'>:</td>").append(
                            "<td valign='top' width='79%'>").append(
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                        );
                        do{
                            file=Sequel.cariIsi("select hasil_pemeriksaan_usg_gambar.photo from hasil_pemeriksaan_usg_gambar where hasil_pemeriksaan_usg_gambar.no_rawat='"+norawat+"'");
                            htmlContent.append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "YANG MELAKUKAN PENGKAJIAN").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='30%' border='0'>Tanggal : ").append(rs2.getString("tanggal")).append("</td>").append(
                                              "<td width='35%' border='0'>Dokter : ").append(rs2.getString("kd_dokter")).append(" ").append(rs2.getString("nm_dokter")).append("</td>").append(
                                              "<td width='35%' border='0'>Kiriman Dari : ").append(rs2.getString("kiriman_dari")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='30%' border='0'>Jenis Prestasi : ").append(rs2.getString("jenis_prestasi")).append("</td>").append(
                                              "<td width='35%' border='0'>HTA : ").append(rs2.getString("hta")).append("</td>").append(
                                              "<td width='35%' border='0'>Diagnosa Klinis : ").append(rs2.getString("diagnosa_klinis")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>"
                            ); 
                            
                            if(!file.equals("")){
                                htmlContent.append(
                                    "<tr>").append(
                                        "<td valign='top'>").append(
                                           "PHOTO USG").append(  
                                           "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                              "<tr>").append(
                                                  "<td valign='top' border='0' width='100%' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanusg/").append(file).append("'><img alt='Gambar USG' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanusg/").append(file).append("' width='450' height='450'/></a></td>").append(
                                              "</tr>").append(
                                           "</table>").append(
                                        "</td>").append(
                                    "</tr>"
                                );
                            }
                            
                            htmlContent.append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "HASIL BACAAN").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='34%'>Ukuran Kantong Gestasi (GS) : ").append(rs2.getString("kantong_gestasi")).append("</td>").append(
                                              "<td width='33%'>Ukuran Bokong - Kepala (CRL) : ").append(rs2.getString("ukuran_bokongkepala")).append("</td>").append(
                                              "<td width='33%'>Diameter Biparietal (DBP) : ").append(rs2.getString("diameter_biparietal")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='34%'>Panjang Femur (FL) : ").append(rs2.getString("panjang_femur")).append("</td>").append(
                                              "<td width='33%'>Lingkar Abdomen (AC) : ").append(rs2.getString("lingkar_abdomen")).append("</td>").append(
                                              "<td width='33%'>Tafsiran berat Janin (TBJ) : ").append(rs2.getString("tafsiran_berat_janin")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='34%'>Usia Kehamilan Sesuai : ").append(rs2.getString("usia_kehamilan")).append("</td>").append(
                                              "<td width='66%' colspan='2'>Plasenta Berimplatansi Di : ").append(rs2.getString("plasenta_berimplatansi")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='34%'>Derajat Maturitas Plasenta : ").append(rs2.getString("derajat_maturitas")).append("</td>").append(
                                              "<td width='33%'>Jumlah Air Ketuban : ").append(rs2.getString("jumlah_air_ketuban")).append("</td>").append(
                                              "<td width='33%'>Peluang Sex : ").append(rs2.getString("peluang_sex")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='34%'>Indeks Cairan Ketuban (ICK) : ").append(rs2.getString("indek_cairan_ketuban")).append("</td>").append(
                                              "<td width='66%' colspan='2'>Kelainan Kongenital Mayor : ").append(rs2.getString("kelainan_kongenital")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%' colspan='3'>Kesimpulan : ").append(rs2.getString("kesimpulan")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>"
                            );
                        }while(rs2.next());
                        htmlContent.append(
                              "</table>").append(
                            "</td>").append(
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Asuhan Medis Rawat Jalan : "+e);
        }
    }

    private void menampilkanBerkasDigital(String norawat) {
        try {
            if(chkBerkasDigital.isSelected()==true){
                try{
                    rs2=koneksi.prepareStatement(
                        "select master_berkas_digital.nama,berkas_digital_perawatan.lokasi_file "+
                        "from berkas_digital_perawatan inner join master_berkas_digital "+
                        "on berkas_digital_perawatan.kode=master_berkas_digital.kode "+
                        "where berkas_digital_perawatan.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){                                    
                        htmlContent.append(
                          "<tr class='isi'>").append( 
                            "<td valign='top' width='2%'></td>").append(        
                            "<td valign='top' width='18%'>Berkas Digital Perawatan</td>").append(
                            "<td valign='top' width='1%' align='center'>:</td>").append(
                            "<td valign='top' width='79%'>").append(
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>").append(
                                 "<tr align='center'>").append(
                                    "<td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td>").append(
                                    "<td valign='top' width='96%' bgcolor='#FFFAF8'>Berkas Digital</td>").append(
                                 "</tr>"
                        );
                        w=1;
                        do{
                            if(rs2.getString("lokasi_file").toLowerCase().contains(".jpg")||rs2.getString("lokasi_file").toLowerCase().contains(".jpeg")){
                                htmlContent.append(
                                     "<tr>").append(
                                        "<td valign='top' align='center'>").append(w).append("</td>").append(
                                        "<td valign='top' align='center'>").append(rs2.getString("nama")).append("<br><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/berkasrawat/").append(rs2.getString("lokasi_file")).append("'><img alt='Berkas Digital' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/berkasrawat/").append(rs2.getString("lokasi_file")).append("' width='450' height='450'/></a></td>").append(
                                     "</tr>"); 
                            }else{
                                htmlContent.append(
                                     "<tr>").append(
                                        "<td valign='top' align='center'>").append(w).append("</td>").append(
                                        "<td valign='top'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/berkasrawat/").append(rs2.getString("lokasi_file")).append("'>").append(rs2.getString("nama")).append("_").append(rs2.getString("lokasi_file").replaceAll("pages/upload/","")).append("</a></td>").append(
                                     "</tr>"); 
                            }
                            w++;
                        }while(rs2.next());
                        htmlContent.append(
                              "</table>").append(
                            "</td>").append(
                          "</tr>");
                    }                                
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        }catch (Exception e) {
            System.out.println("Notif Berkas Digitaln: "+e);
        }
    }
  
    private void menampilkanHasilPemeriksaanUSGUrologi(String norawat) {
        try {
            if(chkHasilPemeriksaanUSGUrologi.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                            "select hasil_pemeriksaan_usg_urologi.kd_dokter,dokter.nm_dokter,hasil_pemeriksaan_usg_urologi.diagnosa_klinis,hasil_pemeriksaan_usg_urologi.kiriman_dari,"+
                            "hasil_pemeriksaan_usg_urologi.kiriman_dari,hasil_pemeriksaan_usg_urologi.ginjal_kanan,hasil_pemeriksaan_usg_urologi.ginjal_kiri,hasil_pemeriksaan_usg_urologi.vesica_urinaria,"+
                            "hasil_pemeriksaan_usg_urologi.tambahan,hasil_pemeriksaan_usg_urologi.tanggal "+
                            "from hasil_pemeriksaan_usg_urologi inner join dokter on hasil_pemeriksaan_usg_urologi.kd_dokter=dokter.kd_dokter "+
                            "where hasil_pemeriksaan_usg_urologi.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>").append( 
                            "<td valign='top' width='2%'></td>").append(        
                            "<td valign='top' width='18%'>Hasil USG Urologi</td>").append(
                            "<td valign='top' width='1%' align='center'>:</td>").append(
                            "<td valign='top' width='79%'>").append(
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                        );
                        do{
                            file=Sequel.cariIsi("select hasil_pemeriksaan_usg_urologi_gambar.photo from hasil_pemeriksaan_usg_urologi_gambar where hasil_pemeriksaan_usg_urologi_gambar.no_rawat='"+norawat+"'");
                            htmlContent.append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "YANG MELAKUKAN PENGKAJIAN").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='50%' border='0'>Tanggal : ").append(rs2.getString("tanggal")).append("</td>").append(
                                              "<td width='50%' border='0'>Dokter : ").append(rs2.getString("kd_dokter")).append(" ").append(rs2.getString("nm_dokter")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='50%' border='0'>Kiriman Dari : ").append(rs2.getString("kiriman_dari")).append("</td>").append(
                                              "<td width='50%' border='0'>Diagnosa Klinis : ").append(rs2.getString("diagnosa_klinis")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>"
                            ); 
                            
                            if(!file.equals("")){
                                htmlContent.append(
                                    "<tr>").append(
                                        "<td valign='top'>").append(
                                           "PHOTO USG").append(  
                                           "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                              "<tr>").append(
                                                  "<td valign='top' border='0' width='100%' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanusgurologi/").append(file).append("'><img alt='Gambar USG' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanusgurologi/").append(file).append("' width='450' height='450'/></a></td>").append(
                                              "</tr>").append(
                                           "</table>").append(
                                        "</td>").append(
                                    "</tr>"
                                );
                            }
                            
                            htmlContent.append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "HASIL BACAAN").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Ginjal Kanan : ").append(rs2.getString("ginjal_kanan").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Ginjal Kiri : ").append(rs2.getString("ginjal_kiri").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Vesica Urinaria : ").append(rs2.getString("vesica_urinaria").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Tambahan : ").append(rs2.getString("tambahan").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>"
                            );
                        }while(rs2.next());
                        htmlContent.append(
                              "</table>").append(
                            "</td>").append(
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Hasil Pemeriksaan USG Urologi : "+e);
        }
    }
    
    private void menampilkanHasilPemeriksaanUSGGynocologi(String norawat) {
        try {
            if(chkHasilPemeriksaanUSGGynecologi.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                            "select hasil_pemeriksaan_usg_gynecologi.tanggal,hasil_pemeriksaan_usg_gynecologi.kd_dokter,dokter.nm_dokter,hasil_pemeriksaan_usg_gynecologi.diagnosa_klinis,hasil_pemeriksaan_usg_gynecologi.kiriman_dari,"+
                            "hasil_pemeriksaan_usg_gynecologi.uterus,hasil_pemeriksaan_usg_gynecologi.parametrium,hasil_pemeriksaan_usg_gynecologi.ovarium,"+
                            "hasil_pemeriksaan_usg_gynecologi.doppler,hasil_pemeriksaan_usg_gynecologi.kesimpulan from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                            "inner join hasil_pemeriksaan_usg_gynecologi on reg_periksa.no_rawat=hasil_pemeriksaan_usg_gynecologi.no_rawat "+
                            "inner join dokter on hasil_pemeriksaan_usg_gynecologi.kd_dokter=dokter.kd_dokter "+
                            "where hasil_pemeriksaan_usg_gynecologi.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>").append( 
                            "<td valign='top' width='2%'></td>").append(        
                            "<td valign='top' width='18%'>Hasil USG Gynecologi</td>").append(
                            "<td valign='top' width='1%' align='center'>:</td>").append(
                            "<td valign='top' width='79%'>").append(
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                        );
                        do{
                            file=Sequel.cariIsi("select hasil_pemeriksaan_usg_gynecologi_gambar.photo from hasil_pemeriksaan_usg_gynecologi_gambar where hasil_pemeriksaan_usg_gynecologi_gambar.no_rawat='"+norawat+"'");
                            htmlContent.append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "YANG MELAKUKAN PENGKAJIAN").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='50%' border='0'>Tanggal : ").append(rs2.getString("tanggal")).append("</td>").append(
                                              "<td width='50%' border='0'>Dokter : ").append(rs2.getString("kd_dokter")).append(" ").append(rs2.getString("nm_dokter")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='50%' border='0'>Kiriman Dari : ").append(rs2.getString("kiriman_dari")).append("</td>").append(
                                              "<td width='50%' border='0'>Diagnosa Klinis : ").append(rs2.getString("diagnosa_klinis")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>"
                            ); 
                            
                            if(!file.equals("")){
                                htmlContent.append(
                                    "<tr>").append(
                                        "<td valign='top'>").append(
                                           "PHOTO USG").append(  
                                           "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                              "<tr>").append(
                                                  "<td valign='top' border='0' width='100%' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanusggynecologi/").append(file).append("'><img alt='Gambar USG' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanusggynecologi/").append(file).append("' width='450' height='450'/></a></td>").append(
                                              "</tr>").append(
                                           "</table>").append(
                                        "</td>").append(
                                    "</tr>"
                                );
                            }
                            
                            htmlContent.append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "HASIL BACAAN").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Uterus : ").append(rs2.getString("uterus").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Parametrium : ").append(rs2.getString("parametrium").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Ovarium : ").append(rs2.getString("ovarium").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Doppler : ").append(rs2.getString("doppler").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Kesimpulan : ").append(rs2.getString("kesimpulan").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>"
                            );
                        }while(rs2.next());
                        htmlContent.append(
                              "</table>").append(
                            "</td>").append(
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Hasil Pemeriksaan USG Gynecologi : "+e);
        }
    }
    
    private void menampilkanHasilPemeriksaanEKG(String norawat) {
        try {
            if(chkHasilPemeriksaanEKG.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                            "select hasil_pemeriksaan_ekg.tanggal,hasil_pemeriksaan_ekg.kd_dokter,dokter.nm_dokter,hasil_pemeriksaan_ekg.diagnosa_klinis,"+
                            "hasil_pemeriksaan_ekg.kiriman_dari,hasil_pemeriksaan_ekg.irama,hasil_pemeriksaan_ekg.laju_jantung,hasil_pemeriksaan_ekg.gelombangp,"+
                            "hasil_pemeriksaan_ekg.intervalpr,hasil_pemeriksaan_ekg.axis,hasil_pemeriksaan_ekg.kompleksqrs,hasil_pemeriksaan_ekg.segmenst,"+
                            "hasil_pemeriksaan_ekg.gelombangt,hasil_pemeriksaan_ekg.kesimpulan from hasil_pemeriksaan_ekg inner join dokter "+
                            "on hasil_pemeriksaan_ekg.kd_dokter=dokter.kd_dokter where hasil_pemeriksaan_ekg.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append("<tr class='isi'>").
                                        append("<td valign='top' width='2%'></td>").
                                        append("<td valign='top' width='18%'>Hasil Pemeriksaan EKG</td>").
                                        append("<td valign='top' width='1%' align='center'>:</td>").
                                        append("<td valign='top' width='79%'>").
                                        append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>");
                        do{
                            file=Sequel.cariIsi("select hasil_pemeriksaan_ekg_gambar.photo from hasil_pemeriksaan_ekg_gambar where hasil_pemeriksaan_ekg_gambar.no_rawat='"+norawat+"'");
                            htmlContent.append("<tr>").
                                            append("<td valign='top'>").
                                                append("YANG MELAKUKAN PENGKAJIAN").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='50%' border='0'>Tanggal : ").append(rs2.getString("tanggal")).append("</td>").
                                                        append("<td width='50%' border='0'>Dokter : ").append(rs2.getString("kd_dokter")).append(" ").append(rs2.getString("nm_dokter")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='50%' border='0'>Kiriman Dari : ").append(rs2.getString("kiriman_dari")).append("</td>").
                                                        append("<td width='50%' border='0'>Diagnosa Klinis : ").append(rs2.getString("diagnosa_klinis")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>"); 
                            if(!file.equals("")){
                                htmlContent.append("<tr>").
                                                append("<td valign='top'>").
                                                    append("PHOTO EKG").
                                                    append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                        append("<tr>").
                                                            append("<td valign='top' border='0' width='100%' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanekg/").append(file).append("'><img alt='Gambar EKG' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanekg/").append(file).append("' width='450' height='450'/></a></td>").
                                                        append("</tr>").
                                                    append("</table>").
                                                append("</td>").
                                            append("</tr>");
                            }
                            htmlContent.append("<tr>").
                                            append("<td valign='top'>").
                                                append("HASIL PEMERIKSAAN").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='50%'>Irama : ").append(rs2.getString("irama")).append("</td>").
                                                        append("<td width='50%'>Laju Jantung : ").append(rs2.getString("laju_jantung")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='50%'>Gelombang P : ").append(rs2.getString("gelombangp")).append("</td>").
                                                        append("<td width='50%'>Interval PR : ").append(rs2.getString("intervalpr")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='50%'>Axis : ").append(rs2.getString("axis")).append("</td>").
                                                        append("<td width='50%'>Kompleks QRS  : ").append(rs2.getString("kompleksqrs")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='50%'>Segmen ST : ").append(rs2.getString("segmenst")).append("</td>").
                                                        append("<td width='50%'>Gelombang T : ").append(rs2.getString("gelombangt")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='100%' colspan='2'>Kesimpulan : ").append(rs2.getString("kesimpulan")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>");
                        }while(rs2.next());
                        htmlContent.append("</table>").
                                append("</td>").
                            append("</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Hasil Pemeriksaan EKG : "+e);
        }
    }
   
    private void menampilkanHasilPemeriksaanUSGNeonatus(String norawat) {
        try {
            if(chkHasilPemeriksaanUSGNeonatus.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                            "select hasil_pemeriksaan_usg_neonatus.tanggal,hasil_pemeriksaan_usg_neonatus.kd_dokter,dokter.nm_dokter,hasil_pemeriksaan_usg_neonatus.diagnosa_klinis,"+
                            "hasil_pemeriksaan_usg_neonatus.kiriman_dari,hasil_pemeriksaan_usg_neonatus.ventrikal_sinistra,hasil_pemeriksaan_usg_neonatus.ventrikal_dextra,hasil_pemeriksaan_usg_neonatus.kesan,"+
                            "hasil_pemeriksaan_usg_neonatus.kesimpulan,hasil_pemeriksaan_usg_neonatus.saran from hasil_pemeriksaan_usg_neonatus inner join dokter on hasil_pemeriksaan_usg_neonatus.kd_dokter=dokter.kd_dokter "+
                            "where hasil_pemeriksaan_usg_neonatus.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>").append( 
                            "<td valign='top' width='2%'></td>").append(        
                            "<td valign='top' width='18%'>Hasil USG Kepala Neonatus</td>").append(
                            "<td valign='top' width='1%' align='center'>:</td>").append(
                            "<td valign='top' width='79%'>").append(
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                        );
                        do{
                            file=Sequel.cariIsi("select hasil_pemeriksaan_usg_neonatus_gambar.photo from hasil_pemeriksaan_usg_neonatus_gambar where hasil_pemeriksaan_usg_neonatus_gambar.no_rawat='"+norawat+"'");
                            htmlContent.append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "YANG MELAKUKAN PENGKAJIAN").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='50%' border='0'>Tanggal : ").append(rs2.getString("tanggal")).append("</td>").append(
                                              "<td width='50%' border='0'>Dokter : ").append(rs2.getString("kd_dokter")).append(" ").append(rs2.getString("nm_dokter")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='50%' border='0'>Kiriman Dari : ").append(rs2.getString("kiriman_dari")).append("</td>").append(
                                              "<td width='50%' border='0'>Diagnosa Klinis : ").append(rs2.getString("diagnosa_klinis")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>"
                            ); 
                            
                            if(!file.equals("")){
                                htmlContent.append(
                                    "<tr>").append(
                                        "<td valign='top'>").append(
                                           "PHOTO USG").append(  
                                           "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                              "<tr>").append(
                                                  "<td valign='top' border='0' width='100%' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanusgneonatus/").append(file).append("'><img alt='Gambar USG' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanusgneonatus/").append(file).append("' width='450' height='450'/></a></td>").append(
                                              "</tr>").append(
                                           "</table>").append(
                                        "</td>").append(
                                    "</tr>"
                                );
                            }
                            
                            htmlContent.append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "PEMERIKSAAN SAGITAL & CORONAL :").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Ventrikel Sinistra : ").append(rs2.getString("ventrikal_sinistra").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Ventrikel Dextra : ").append(rs2.getString("ventrikal_dextra").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Kesan : ").append(rs2.getString("kesan").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Kesimpulan : ").append(rs2.getString("kesimpulan").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Saran : ").append(rs2.getString("saran").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>"
                            );
                        }while(rs2.next());
                        htmlContent.append(
                              "</table>").append(
                            "</td>").append(
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Hasil Pemeriksaan USG Neonatus : "+e);
        }
    }
    
    private void menampilkanHasilEndoskopiFaringLaring(String norawat) {
        try {
            if(chkHasilPemeriksaanEndoskopiFaringLaring.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                            "select hasil_endoskopi_faring_laring.tanggal,hasil_endoskopi_faring_laring.kd_dokter,dokter.nm_dokter,hasil_endoskopi_faring_laring.diagnosa_klinis,hasil_endoskopi_faring_laring.kiriman_dari,"+
                            "hasil_endoskopi_faring_laring.faring_uvula,hasil_endoskopi_faring_laring.faring_arkus_faring,hasil_endoskopi_faring_laring.faring_dinding_posterior,"+
                            "hasil_endoskopi_faring_laring.faring_tonsil,hasil_endoskopi_faring_laring.laring_tonsil_lingual,hasil_endoskopi_faring_laring.laring_valekula,"+
                            "hasil_endoskopi_faring_laring.laring_sinus_piriformis,hasil_endoskopi_faring_laring.laring_epiglotis,hasil_endoskopi_faring_laring.laring_arytenoid,"+
                            "hasil_endoskopi_faring_laring.laring_plika_ventrikularis,hasil_endoskopi_faring_laring.laring_pita_suara,hasil_endoskopi_faring_laring.laring_rima_vocalis,"+
                            "hasil_endoskopi_faring_laring.laring_lainlain,hasil_endoskopi_faring_laring.kesan,hasil_endoskopi_faring_laring.saran "+
                            "from hasil_endoskopi_faring_laring inner join dokter on hasil_endoskopi_faring_laring.kd_dokter=dokter.kd_dokter where hasil_endoskopi_faring_laring.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>").append( 
                            "<td valign='top' width='2%'></td>").append(        
                            "<td valign='top' width='18%'>Hasil Pemeriksaan Tele Endoskopi Faring/Tele Laringoskopi</td>").append(
                            "<td valign='top' width='1%' align='center'>:</td>").append(
                            "<td valign='top' width='79%'>").append(
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                        );
                        do{
                            file=Sequel.cariIsi("select hasil_endoskopi_faring_laring_gambar.photo from hasil_endoskopi_faring_laring_gambar where hasil_endoskopi_faring_laring_gambar.no_rawat='"+norawat+"'");
                            htmlContent.append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "YANG MELAKUKAN PENGKAJIAN").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='50%' border='0'>Tanggal : ").append(rs2.getString("tanggal")).append("</td>").append(
                                              "<td width='50%' border='0'>Dokter : ").append(rs2.getString("kd_dokter")).append(" ").append(rs2.getString("nm_dokter")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='50%' border='0'>Kiriman Dari : ").append(rs2.getString("kiriman_dari")).append("</td>").append(
                                              "<td width='50%' border='0'>Diagnosa Klinis : ").append(rs2.getString("diagnosa_klinis")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>"
                            ); 
                            
                            if(!file.equals("")){
                                htmlContent.append(
                                    "<tr>").append(
                                        "<td valign='top'>").append(
                                           "PHOTO ENDOSKOPI").append(  
                                           "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                              "<tr>").append(
                                                  "<td valign='top' border='0' width='100%' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanendoskopifaringlaring/").append(file).append("'><img alt='Gambar Endoskopi' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanendoskopifaringlaring/").append(file).append("' width='450' height='450'/></a></td>").append(
                                              "</tr>").append(
                                           "</table>").append(
                                        "</td>").append(
                                    "</tr>"
                                );
                            }
                            
                            htmlContent.append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "FARING :").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Dinding Posterior : ").append(rs2.getString("faring_dinding_posterior")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Uvula : ").append(rs2.getString("faring_uvula")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Arkus Faring : ").append(rs2.getString("faring_arkus_faring")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Tonsil : ").append(rs2.getString("faring_tonsil")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>").append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "LARING :").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Tonsil Lingual : ").append(rs2.getString("laring_tonsil_lingual")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Valekula : ").append(rs2.getString("laring_valekula")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Sinus Piriformis : ").append(rs2.getString("laring_sinus_piriformis")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Epiglotis : ").append(rs2.getString("laring_epiglotis")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Plika Ventrikularis : ").append(rs2.getString("laring_plika_ventrikularis")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Arytenoid : ").append(rs2.getString("laring_arytenoid")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Rima Vocalis : ").append(rs2.getString("laring_rima_vocalis")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Pita Suara : ").append(rs2.getString("laring_pita_suara")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='100%'>Lain-lain : ").append(rs2.getString("laring_lainlain")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>").append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "KESAN :").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='100%'>").append(rs2.getString("kesan").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>").append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "SARAN :").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='100%'>").append(rs2.getString("saran").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>"
                            );
                        }while(rs2.next());
                        htmlContent.append(
                              "</table>").append(
                            "</td>").append(
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Hasil Pemeriksaan Endoskopi Faring/Laring : "+e);
        }
    }
    
    private void menampilkanHasilEndoskopiHidung(String norawat) {
        try {
            if(chkHasilPemeriksaanEndoskopiHidung.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                            "select hasil_endoskopi_hidung.tanggal,hasil_endoskopi_hidung.kd_dokter,dokter.nm_dokter,hasil_endoskopi_hidung.diagnosa_klinis,hasil_endoskopi_hidung.kiriman_dari,"+
                            "hasil_endoskopi_hidung.kondisi_hidung_kanan,hasil_endoskopi_hidung.kondisi_hidung_kiri,hasil_endoskopi_hidung.kavum_nasi_kanan,"+
                            "hasil_endoskopi_hidung.kavum_nasi_kiri,hasil_endoskopi_hidung.konka_inferior_kanan,hasil_endoskopi_hidung.konka_inferior_kiri,"+
                            "hasil_endoskopi_hidung.meatus_medius_kanan,hasil_endoskopi_hidung.meatus_medius_kiri,hasil_endoskopi_hidung.septum_kanan,"+
                            "hasil_endoskopi_hidung.septum_kiri,hasil_endoskopi_hidung.nasofaring_kanan,hasil_endoskopi_hidung.nasofaring_kiri,"+
                            "hasil_endoskopi_hidung.lainlain_kanan,hasil_endoskopi_hidung.lainlain_kiri,hasil_endoskopi_hidung.kesimpulan "+
                            "from hasil_endoskopi_hidung inner join dokter on hasil_endoskopi_hidung.kd_dokter=dokter.kd_dokter where hasil_endoskopi_hidung.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>").append( 
                            "<td valign='top' width='2%'></td>").append(        
                            "<td valign='top' width='18%'>Hasil Pemeriksaan Tele Endoskopi Hidung</td>").append(
                            "<td valign='top' width='1%' align='center'>:</td>").append(
                            "<td valign='top' width='79%'>").append(
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                        );
                        do{
                            file=Sequel.cariIsi("select hasil_endoskopi_hidung_gambar.photo from hasil_endoskopi_hidung_gambar where hasil_endoskopi_hidung_gambar.no_rawat='"+norawat+"'");
                            htmlContent.append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "YANG MELAKUKAN PENGKAJIAN").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='50%' border='0'>Tanggal : ").append(rs2.getString("tanggal")).append("</td>").append(
                                              "<td width='50%' border='0'>Dokter : ").append(rs2.getString("kd_dokter")).append(" ").append(rs2.getString("nm_dokter")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='50%' border='0'>Kiriman Dari : ").append(rs2.getString("kiriman_dari")).append("</td>").append(
                                              "<td width='50%' border='0'>Diagnosa Klinis : ").append(rs2.getString("diagnosa_klinis")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>"
                            ); 
                            
                            if(!file.equals("")){
                                htmlContent.append(
                                    "<tr>").append(
                                        "<td valign='top'>").append(
                                           "PHOTO ENDOSKOPI").append(  
                                           "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                              "<tr>").append(
                                                  "<td valign='top' border='0' width='100%' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanendoskopihidung/").append(file).append("'><img alt='Gambar Endoskopi' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanendoskopihidung/").append(file).append("' width='450' height='450'/></a></td>").append(
                                              "</tr>").append(
                                           "</table>").append(
                                        "</td>").append(
                                    "</tr>"
                                );
                            }
                            
                            htmlContent.append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "HASIL PEMERIKSAAN NASOLARINGOSKOPI :").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='30%' align='center'>Pemeriksaan</td>").append(
                                              "<td width='35%' align='center'>Kanan</td>").append(
                                              "<td width='35%' align='center'>Kiri</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td>Hidung</td>").append(
                                              "<td align='center'>").append(rs2.getString("kondisi_hidung_kanan")).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("kondisi_hidung_kiri")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td>- Kavum Nasi</td>").append(
                                              "<td align='center'>").append(rs2.getString("kavum_nasi_kanan")).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("kavum_nasi_kiri")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td>- Konka Inferior</td>").append(
                                              "<td align='center'>").append(rs2.getString("konka_inferior_kanan")).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("konka_inferior_kiri")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td>- Meatus Medius</td>").append(
                                              "<td align='center'>").append(rs2.getString("meatus_medius_kanan")).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("meatus_medius_kiri")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td>- Septum</td>").append(
                                              "<td align='center'>").append(rs2.getString("septum_kanan")).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("septum_kiri")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td>- Nasofaring</td>").append(
                                              "<td align='center'>").append(rs2.getString("nasofaring_kanan")).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("nasofaring_kiri")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td>- Lain-lain</td>").append(
                                              "<td align='center'>").append(rs2.getString("lainlain_kanan")).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("lainlain_kiri")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>").append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "KESIMPULAN :").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='100%' border='0'>").append(rs2.getString("kesimpulan").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>"
                            );
                        }while(rs2.next());
                        htmlContent.append(
                              "</table>").append(
                            "</td>").append(
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Hasil Pemeriksaan Endoskopi Hidung : "+e);
        }
    }
    
    private void menampilkanHasilEndoskopiTelinga(String norawat) {
        try {
            if(chkHasilPemeriksaanEndoskopiTelinga.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                            "select hasil_endoskopi_telinga.tanggal,hasil_endoskopi_telinga.kd_dokter,dokter.nm_dokter,hasil_endoskopi_telinga.diagnosa_klinis,"+
                            "hasil_endoskopi_telinga.kiriman_dari,hasil_endoskopi_telinga.bentuk_liang_telinga_kanan,hasil_endoskopi_telinga.bentuk_liang_telinga_kiri,"+
                            "hasil_endoskopi_telinga.kondisi_liang_telinga_kanan,hasil_endoskopi_telinga.keterangan_kondisi_liang_telinga_kanan,"+
                            "hasil_endoskopi_telinga.kondisi_liang_telinga_kiri,hasil_endoskopi_telinga.keterangan_kondisi_liang_telinga_kiri,"+
                            "hasil_endoskopi_telinga.membran_timpani_intak_kanan,hasil_endoskopi_telinga.membran_timpani_intak_kiri,"+
                            "hasil_endoskopi_telinga.membran_timpani_perforasi_kanan,hasil_endoskopi_telinga.keterangan_membran_timpani_perforasi_kanan,"+
                            "hasil_endoskopi_telinga.membran_timpani_perforasi_kiri,hasil_endoskopi_telinga.keterangan_membran_timpani_perforasi_kiri,"+
                            "hasil_endoskopi_telinga.kavum_timpani_mukosa_kanan,hasil_endoskopi_telinga.kavum_timpani_mukosa_kiri,"+
                            "hasil_endoskopi_telinga.kavum_timpani_osikel_kanan,hasil_endoskopi_telinga.kavum_timpani_osikel_kiri,"+
                            "hasil_endoskopi_telinga.kavum_timpani_isthmus_kanan,hasil_endoskopi_telinga.kavum_timpani_isthmus_kiri,"+
                            "hasil_endoskopi_telinga.kavum_timpani_anterior_kanan,hasil_endoskopi_telinga.kavum_timpani_anterior_kiri,"+
                            "hasil_endoskopi_telinga.kavum_timpani_posterior_kanan,hasil_endoskopi_telinga.kavum_timpani_posterior_kiri,"+
                            "hasil_endoskopi_telinga.lainlain_kanan,hasil_endoskopi_telinga.lainlain_kiri,hasil_endoskopi_telinga.kesimpulan,"+
                            "hasil_endoskopi_telinga.anjuran from hasil_endoskopi_telinga inner join dokter on hasil_endoskopi_telinga.kd_dokter=dokter.kd_dokter "+
                            "where hasil_endoskopi_telinga.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>").append( 
                            "<td valign='top' width='2%'></td>").append(        
                            "<td valign='top' width='18%'>Hasil Pemeriksaan Tele Endoskopi Telinga</td>").append(
                            "<td valign='top' width='1%' align='center'>:</td>").append(
                            "<td valign='top' width='79%'>").append(
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                        );
                        do{
                            file=Sequel.cariIsi("select hasil_endoskopi_telinga_gambar.photo from hasil_endoskopi_telinga_gambar where hasil_endoskopi_telinga_gambar.no_rawat='"+norawat+"'");
                            htmlContent.append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "YANG MELAKUKAN PENGKAJIAN").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='50%' border='0'>Tanggal : ").append(rs2.getString("tanggal")).append("</td>").append(
                                              "<td width='50%' border='0'>Dokter : ").append(rs2.getString("kd_dokter")).append(" ").append(rs2.getString("nm_dokter")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td width='50%' border='0'>Kiriman Dari : ").append(rs2.getString("kiriman_dari")).append("</td>").append(
                                              "<td width='50%' border='0'>Diagnosa Klinis : ").append(rs2.getString("diagnosa_klinis")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>"
                            ); 
                            
                            if(!file.equals("")){
                                htmlContent.append(
                                    "<tr>").append(
                                        "<td valign='top'>").append(
                                           "PHOTO ENDOSKOPI").append(  
                                           "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                              "<tr>").append(
                                                  "<td valign='top' border='0' width='100%' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanendoskopitelinga/").append(file).append("'><img alt='Gambar Endoskopi' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanendoskopitelinga/").append(file).append("' width='450' height='450'/></a></td>").append(
                                              "</tr>").append(
                                           "</table>").append(
                                        "</td>").append(
                                    "</tr>"
                                );
                            }
                            
                            htmlContent.append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "HASIL PEMERIKSAAN MIKROSKOPIK/ENDOSKOPI TELINGA :").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='30%' align='center'>Pemeriksaan</td>").append(
                                              "<td width='35%' align='center'>Kanan</td>").append(
                                              "<td width='35%' align='center'>Kiri</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td>Telinga</td>").append(
                                              "<td align='center'>").append(rs2.getString("bentuk_liang_telinga_kanan")).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("bentuk_liang_telinga_kiri")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td>- Liang Telinga</td>").append(
                                              "<td align='center'>").append(rs2.getString("kondisi_liang_telinga_kanan")).append((rs2.getString("keterangan_kondisi_liang_telinga_kanan").equals("")?"":", "+rs2.getString("keterangan_kondisi_liang_telinga_kanan"))).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("kondisi_liang_telinga_kiri")).append((rs2.getString("keterangan_kondisi_liang_telinga_kiri").equals("")?"":", "+rs2.getString("keterangan_kondisi_liang_telinga_kiri"))).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td>- Membran Timpani</td>").append(
                                              "<td align='center'>&nbsp;</td>").append(
                                              "<td align='center'>&nbsp;</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td style='margin-left: 15px'>Perforasi</td>").append(
                                              "<td align='center'>").append(rs2.getString("membran_timpani_perforasi_kanan")).append((rs2.getString("keterangan_membran_timpani_perforasi_kanan").equals("")?"":", "+rs2.getString("keterangan_membran_timpani_perforasi_kanan"))).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("membran_timpani_perforasi_kiri")).append((rs2.getString("keterangan_membran_timpani_perforasi_kiri").equals("")?"":", "+rs2.getString("keterangan_membran_timpani_perforasi_kiri"))).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td style='margin-left: 15px'>Intak</td>").append(
                                              "<td align='center'>").append(rs2.getString("membran_timpani_intak_kanan")).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("membran_timpani_intak_kiri")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td>- Kavum Timpani</td>").append(
                                              "<td align='center'>&nbsp;</td>").append(
                                              "<td align='center'>&nbsp;</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td style='margin-left: 15px'>Mukosa</td>").append(
                                              "<td align='center'>").append(rs2.getString("kavum_timpani_mukosa_kanan")).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("kavum_timpani_mukosa_kiri")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td style='margin-left: 15px'>Osikel</td>").append(
                                              "<td align='center'>").append(rs2.getString("kavum_timpani_osikel_kanan")).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("kavum_timpani_osikel_kiri")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td style='margin-left: 15px'>Isthmus</td>").append(
                                              "<td align='center'>").append(rs2.getString("kavum_timpani_isthmus_kanan")).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("kavum_timpani_isthmus_kiri")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td style='margin-left: 15px'>Anterior</td>").append(
                                              "<td align='center'>").append(rs2.getString("kavum_timpani_anterior_kanan")).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("kavum_timpani_anterior_kiri")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td style='margin-left: 15px'>Posterior</td>").append(
                                              "<td align='center'>").append(rs2.getString("kavum_timpani_posterior_kanan")).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("kavum_timpani_posterior_kiri")).append("</td>").append(
                                          "</tr>").append(
                                          "<tr>").append(
                                              "<td>- Lain-lain</td>").append(
                                              "<td align='center'>").append(rs2.getString("lainlain_kanan")).append("</td>").append(
                                              "<td align='center'>").append(rs2.getString("lainlain_kiri")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>").append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "KESIMPULAN :").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='100%' border='0'>").append(rs2.getString("kesimpulan").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>").append(
                                 "<tr>").append(
                                    "<td valign='top'>").append(
                                       "ANJURAN :").append(  
                                       "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").append(
                                          "<tr>").append(
                                              "<td width='100%' border='0'>").append(rs2.getString("anjuran").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").append(
                                          "</tr>").append(
                                       "</table>").append(
                                    "</td>").append(
                                 "</tr>"
                            );
                        }while(rs2.next());
                        htmlContent.append(
                              "</table>").append(
                            "</td>").append(
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Hasil Pemeriksaan Endoskopi Telinga : "+e);
        }
    }
     
    private void menampilkanHasilPemeriksaanECHO(String norawat) {
        try {
            if(chkHasilPemeriksaanEcho.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                            "select hasil_pemeriksaan_echo.tanggal,hasil_pemeriksaan_echo.kd_dokter,dokter.nm_dokter,hasil_pemeriksaan_echo.sistolik,hasil_pemeriksaan_echo.diastolic,hasil_pemeriksaan_echo.kontraktilitas,"+
                            "hasil_pemeriksaan_echo.dimensi_ruang,hasil_pemeriksaan_echo.katup,hasil_pemeriksaan_echo.analisa_segmental,hasil_pemeriksaan_echo.erap,hasil_pemeriksaan_echo.lain_lain,hasil_pemeriksaan_echo.kesimpulan "+
                            "from hasil_pemeriksaan_echo inner join dokter on hasil_pemeriksaan_echo.kd_dokter=dokter.kd_dokter where hasil_pemeriksaan_echo.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append("<tr class='isi'>").
                                    append("<td valign='top' width='2%'></td>").
                                    append("<td valign='top' width='18%'>Hasil Pemeriksaan ECHO</td>").
                                    append("<td valign='top' width='1%' align='center'>:</td>").
                                    append("<td valign='top' width='79%'>").
                                    append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>");
                        do{
                            file=Sequel.cariIsi("select hasil_pemeriksaan_echo_gambar.photo from hasil_pemeriksaan_echo_gambar where hasil_pemeriksaan_echo_gambar.no_rawat='"+norawat+"'");
                            htmlContent.append("<tr>").
                                            append("<td valign='top'>").
                                                append("YANG MELAKUKAN PENGKAJIAN").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='50%' border='0'>Tanggal : ").append(rs2.getString("tanggal")).append("</td>").
                                                        append("<td width='50%' border='0'>Dokter : ").append(rs2.getString("kd_dokter")).append(" ").append(rs2.getString("nm_dokter")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>"); 
                            if(!file.equals("")){
                                htmlContent.append("<tr>").
                                                append("<td valign='top'>").
                                                    append("PHOTO ECHO").
                                                    append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                        append("<tr>").
                                                            append("<td valign='top' border='0' width='100%' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanecho/").append(file).append("'><img alt='Gambar EKG' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanecho/").append(file).append("' width='450' height='450'/></a></td>").
                                                        append("</tr>").
                                                    append("</table>").
                                                append("</td>").
                                            append("</tr>");
                            }
                            
                            htmlContent.append("<tr>").
                                            append("<td valign='top'>").
                                                append("HASIL PEMERIKSAAN").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='50%'>Fungsi Sistolik LV : ").append(rs2.getString("sistolik")).append("</td>").
                                                        append("<td width='50%'>Fungsi Diastolik LV : ").append(rs2.getString("diastolic")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='50%'>Kontaktilitas RV : ").append(rs2.getString("kontraktilitas")).append("</td>").
                                                        append("<td width='50%'>Dimensi Ruang Jantung : ").append(rs2.getString("dimensi_ruang")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='50%'>Katup-katup : ").append(rs2.getString("katup")).append("</td>").
                                                        append("<td width='50%'>Analisa Segmental : ").append(rs2.getString("analisa_segmental")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='50%'>eRAP : ").append(rs2.getString("erap")).append("</td>").
                                                        append("<td width='50%'>Lain-lain : ").append(rs2.getString("lain_lain")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='100%' colspan='2'>Kesimpulan : ").append(rs2.getString("kesimpulan")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>");
                        }while(rs2.next());
                        htmlContent.append("</table>").
                                    append("</td>").
                                    append("</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Hasil Pemeriksaan ECHO : "+e);
        }
    }
      
    private void menampilkanHasilPemeriksaanSlitLamp(String norawat) {
        try {
            if(chkHasilPemeriksaanSlitLamp.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                            "select hasil_pemeriksaan_slit_lamp.tanggal,hasil_pemeriksaan_slit_lamp.kd_dokter,dokter.nm_dokter,hasil_pemeriksaan_slit_lamp.diagnosa_klinis,hasil_pemeriksaan_slit_lamp.kiriman_dari,"+
                            "hasil_pemeriksaan_slit_lamp.hasil_pemeriksaan from hasil_pemeriksaan_slit_lamp inner join dokter on hasil_pemeriksaan_slit_lamp.kd_dokter=dokter.kd_dokter "+
                            "where hasil_pemeriksaan_slit_lamp.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append("<tr class='isi'>").
                                        append("<td valign='top' width='2%'></td>").
                                        append("<td valign='top' width='18%'>Hasil Pemeriksaan Slit Lamp</td>").
                                        append("<td valign='top' width='1%' align='center'>:</td>").
                                        append("<td valign='top' width='79%'>").
                                        append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>");
                        do{
                            file=Sequel.cariIsi("select hasil_pemeriksaan_slit_lamp_gambar.photo from hasil_pemeriksaan_slit_lamp_gambar where hasil_pemeriksaan_slit_lamp_gambar.no_rawat='"+norawat+"'");
                            htmlContent.append("<tr>").
                                            append("<td valign='top'>").
                                                append("YANG MELAKUKAN PENGKAJIAN").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='40%' border='0'>Tanggal : ").append(rs2.getString("tanggal")).append("</td>").
                                                        append("<td width='60%' border='0'>Dokter : ").append(rs2.getString("kd_dokter")).append(" ").append(rs2.getString("nm_dokter")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='40%' border='0'>Kiriman Dari : ").append(rs2.getString("kiriman_dari")).append("</td>").
                                                        append("<td width='60%' border='0'>Diagnosa Klinis : ").append(rs2.getString("diagnosa_klinis")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>"); 
                            if(!file.equals("")){
                                htmlContent.append("<tr>").
                                                append("<td valign='top'>").
                                                    append("PHOTO SLIT LAMP").
                                                    append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                        append("<tr>").
                                                            append("<td valign='top' border='0' width='100%' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanslitlamp/").append(file).append("'><img alt='Gambar Slit Lamp' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanslitlamp/").append(file).append("' width='450' height='450'/></a></td>").
                                                        append("</tr>").
                                                    append("</table>").
                                                append("</td>").
                                            append("</tr>");
                            }
                            htmlContent.append("<tr>").
                                            append("<td valign='top'>").
                                                append("HASIL PEMERIKSAAN").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='100%' border='0'>").append(rs2.getString("hasil_pemeriksaan")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>");
                        }while(rs2.next());
                        htmlContent.append("</table>").
                                append("</td>").
                            append("</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Hasil Pemeriksaan Slit Lamp : "+e);
        }
    }
    
    private void menampilkanHasilPemeriksaanOCT(String norawat) {
        try {
            if(chkHasilPemeriksaanOCT.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                            "select hasil_pemeriksaan_oct.tanggal,hasil_pemeriksaan_oct.kd_dokter,dokter.nm_dokter,hasil_pemeriksaan_oct.diagnosa_klinis,hasil_pemeriksaan_oct.kiriman_dari,"+
                            "hasil_pemeriksaan_oct.hasil_pemeriksaan from hasil_pemeriksaan_oct inner join dokter on hasil_pemeriksaan_oct.kd_dokter=dokter.kd_dokter "+
                            "where hasil_pemeriksaan_oct.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append("<tr class='isi'>").
                                        append("<td valign='top' width='2%'></td>").
                                        append("<td valign='top' width='18%'>Hasil Pemeriksaan OCT</td>").
                                        append("<td valign='top' width='1%' align='center'>:</td>").
                                        append("<td valign='top' width='79%'>").
                                        append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>");
                        do{
                            file=Sequel.cariIsi("select hasil_pemeriksaan_oct_gambar.photo from hasil_pemeriksaan_oct_gambar where hasil_pemeriksaan_oct_gambar.no_rawat='"+norawat+"'");
                            htmlContent.append("<tr>").
                                            append("<td valign='top'>").
                                                append("YANG MELAKUKAN PENGKAJIAN").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='40%' border='0'>Tanggal : ").append(rs2.getString("tanggal")).append("</td>").
                                                        append("<td width='60%' border='0'>Dokter : ").append(rs2.getString("kd_dokter")).append(" ").append(rs2.getString("nm_dokter")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='40%' border='0'>Kiriman Dari : ").append(rs2.getString("kiriman_dari")).append("</td>").
                                                        append("<td width='60%' border='0'>Diagnosa Klinis : ").append(rs2.getString("diagnosa_klinis")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>"); 
                            if(!file.equals("")){
                                htmlContent.append("<tr>").
                                                append("<td valign='top'>").
                                                    append("PHOTO OCT").
                                                    append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                        append("<tr>").
                                                            append("<td valign='top' border='0' width='100%' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanoct/").append(file).append("'><img alt='Gambar OCT' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanoct/").append(file).append("' width='450' height='450'/></a></td>").
                                                        append("</tr>").
                                                    append("</table>").
                                                append("</td>").
                                            append("</tr>");
                            }
                            htmlContent.append("<tr>").
                                            append("<td valign='top'>").
                                                append("HASIL PEMERIKSAAN").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='100%' border='0'>").append(rs2.getString("hasil_pemeriksaan")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>");
                        }while(rs2.next());
                        htmlContent.append("</table>").
                                append("</td>").
                            append("</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Hasil Pemeriksaan OCT : "+e);
        }
    }

    private void menampilkanHasilPemeriksaanTreadmill(String norawat) {
        try {
            if(chkHasilPemeriksaanTreadmill.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                            "select hasil_pemeriksaan_treadmill.tanggal,hasil_pemeriksaan_treadmill.kd_dokter,dokter.nm_dokter,hasil_pemeriksaan_treadmill.kiriman_dari,hasil_pemeriksaan_treadmill.diagnosa_klinis,"+
                            "hasil_pemeriksaan_treadmill.protokol,hasil_pemeriksaan_treadmill.keterangan_protokol,hasil_pemeriksaan_treadmill.td_awal,hasil_pemeriksaan_treadmill.nadi_awal,"+
                            "hasil_pemeriksaan_treadmill.denyut_jantung_maksimal,hasil_pemeriksaan_treadmill.hasil_pemeriksaan,hasil_pemeriksaan_treadmill.temuan_ekg,"+
                            "hasil_pemeriksaan_treadmill.kapasitas_fungsional,hasil_pemeriksaan_treadmill.interpretasi,hasil_pemeriksaan_treadmill.kesimpulan "+
                            "from hasil_pemeriksaan_treadmill inner join dokter on hasil_pemeriksaan_treadmill.kd_dokter=dokter.kd_dokter where hasil_pemeriksaan_treadmill.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append("<tr class='isi'>").
                                        append("<td valign='top' width='2%'></td>").
                                        append("<td valign='top' width='18%'>Hasil Pemeriksaan Treadmill</td>").
                                        append("<td valign='top' width='1%' align='center'>:</td>").
                                        append("<td valign='top' width='79%'>").
                                        append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>");
                        do{
                            file=Sequel.cariIsi("select hasil_pemeriksaan_treadmill_gambar.photo from hasil_pemeriksaan_treadmill_gambar where hasil_pemeriksaan_treadmill_gambar.no_rawat='"+norawat+"'");
                            htmlContent.append("<tr>").
                                            append("<td valign='top'>").
                                                append("YANG MELAKUKAN PENGKAJIAN").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='50%' border='0'>Tanggal : ").append(rs2.getString("tanggal")).append("</td>").
                                                        append("<td width='50%' border='0'>Dokter : ").append(rs2.getString("kd_dokter")).append(" ").append(rs2.getString("nm_dokter")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='50%' border='0'>Kiriman Dari : ").append(rs2.getString("kiriman_dari")).append("</td>").
                                                        append("<td width='50%' border='0'>Diagnosa Klinis : ").append(rs2.getString("diagnosa_klinis")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>"); 
                            if(!file.equals("")){
                                htmlContent.append("<tr>").
                                                append("<td valign='top'>").
                                                    append("PHOTO TREADMILL").
                                                    append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                        append("<tr>").
                                                            append("<td valign='top' border='0' width='100%' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaantreadmill/").append(file).append("'><img alt='Gambar EKG' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaantreadmill/").append(file).append("' width='450' height='450'/></a></td>").
                                                        append("</tr>").
                                                    append("</table>").
                                                append("</td>").
                                            append("</tr>");
                            }
                            htmlContent.append("<tr>").
                                            append("<td valign='top'>").
                                                append("PROTOKOL YANG DIGUNAKAN & PARAMETER DASAR").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='50%'>Protokol : ").append(rs2.getString("protokol")).append((rs2.getString("keterangan_protokol").equals("")?"":", "+rs2.getString("keterangan_protokol"))).append("</td>").
                                                        append("<td width='50%'>TD Awal : ").append(rs2.getString("td_awal")).append(" mmHg</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='50%'>Nadi Awal : ").append(rs2.getString("nadi_awal")).append(" x/menit</td>").
                                                        append("<td width='50%'>Denyut Jantung Maksimal Teoritis : ").append(rs2.getString("denyut_jantung_maksimal")).append(" x/menit</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>").
                                        append("<tr>").
                                            append("<td valign='top'>").
                                                append("HASIL PEMERIKSAAN").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='100%' border='0'>").append(rs2.getString("hasil_pemeriksaan")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>").
                                        append("<tr>").
                                            append("<td valign='top'>").
                                                append("TEMUAN EKG").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='100%' border='0'>").append(rs2.getString("temuan_ekg")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>").
                                        append("<tr>").
                                            append("<td valign='top'>").
                                                append("KAPASITAS FUNGSIONAL").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='100%' border='0'>").append(rs2.getString("kapasitas_fungsional")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>").
                                        append("<tr>").
                                            append("<td valign='top'>").
                                                append("INTERPRETASI").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='100%' border='0'>").append(rs2.getString("interpretasi")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>").
                                        append("<tr>").
                                            append("<td valign='top'>").
                                                append("KESIMPULAN").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='100%' border='0'>").append(rs2.getString("kesimpulan")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>");
                        }while(rs2.next());
                        htmlContent.append("</table>").
                                append("</td>").
                            append("</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Hasil Pemeriksaan Treadmill : "+e);
        }
    }
    
    private void menampilkanHasilPemeriksaanECHOPediatrik(String norawat) {
        try {
            if(chkHasilPemeriksaanEchoPediatrik.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                            "select hasil_pemeriksaan_echo_pediatrik.tanggal,hasil_pemeriksaan_echo_pediatrik.kd_dokter,dokter.nm_dokter,hasil_pemeriksaan_echo_pediatrik.diagnosa_klinis,hasil_pemeriksaan_echo_pediatrik.kiriman_dari,"+
                            "hasil_pemeriksaan_echo_pediatrik.situs,hasil_pemeriksaan_echo_pediatrik.av_va,hasil_pemeriksaan_echo_pediatrik.drainase_vena_pulmonalis,hasil_pemeriksaan_echo_pediatrik.katup_mitral,hasil_pemeriksaan_echo_pediatrik.katup_aorta,"+
                            "hasil_pemeriksaan_echo_pediatrik.katup_tricuspid,hasil_pemeriksaan_echo_pediatrik.katup_pulmonal,hasil_pemeriksaan_echo_pediatrik.katup_septum_atrium,hasil_pemeriksaan_echo_pediatrik.katup_septum_ventrikal,"+
                            "hasil_pemeriksaan_echo_pediatrik.katup_arkus_aorta,hasil_pemeriksaan_echo_pediatrik.katup_keterangan_lainnya,hasil_pemeriksaan_echo_pediatrik.ruang_jantung,hasil_pemeriksaan_echo_pediatrik.mode_ivds,"+
                            "hasil_pemeriksaan_echo_pediatrik.mode_ivss,hasil_pemeriksaan_echo_pediatrik.mode_lvid_dextra,hasil_pemeriksaan_echo_pediatrik.mode_lvid_sinistra,hasil_pemeriksaan_echo_pediatrik.mode_lvpw_dextra,hasil_pemeriksaan_echo_pediatrik.mode_lvpw_sinistra,"+
                            "hasil_pemeriksaan_echo_pediatrik.mode_ejection_fraction,hasil_pemeriksaan_echo_pediatrik.mode_fraction_shotening,hasil_pemeriksaan_echo_pediatrik.doppler,hasil_pemeriksaan_echo_pediatrik.kesimpulan,hasil_pemeriksaan_echo_pediatrik.saran "+
                            "from hasil_pemeriksaan_echo_pediatrik inner join dokter on hasil_pemeriksaan_echo_pediatrik.kd_dokter=dokter.kd_dokter where hasil_pemeriksaan_echo_pediatrik.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append("<tr class='isi'>").
                                    append("<td valign='top' width='2%'></td>").
                                    append("<td valign='top' width='18%'>Hasil Pemeriksaan ECHO Pediatrik</td>").
                                    append("<td valign='top' width='1%' align='center'>:</td>").
                                    append("<td valign='top' width='79%'>").
                                    append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>");
                        do{
                            file=Sequel.cariIsi("select hasil_pemeriksaan_echo_pediatrik_gambar.photo from hasil_pemeriksaan_echo_pediatrik_gambar where hasil_pemeriksaan_echo_pediatrik_gambar.no_rawat='"+norawat+"'");
                            htmlContent.append("<tr>").
                                            append("<td valign='top'>").
                                                append("YANG MELAKUKAN PENGKAJIAN").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='40%' border='0'>Tanggal : ").append(rs2.getString("tanggal")).append("</td>").
                                                        append("<td width='60%' border='0'>Dokter : ").append(rs2.getString("kd_dokter")).append(" ").append(rs2.getString("nm_dokter")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='40%' border='0'>Kiriman Dari : ").append(rs2.getString("kiriman_dari")).append("</td>").
                                                        append("<td width='60%' border='0'>Diagnosa Klinis : ").append(rs2.getString("diagnosa_klinis")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>"); 
                            if(!file.equals("")){
                                htmlContent.append("<tr>").
                                                append("<td valign='top'>").
                                                    append("PHOTO ECHO").
                                                    append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                        append("<tr>").
                                                            append("<td valign='top' border='0' width='100%' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanechopediatrik/").append(file).append("'><img alt='Gambar EKG' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanechopediatrik/").append(file).append("' width='450' height='450'/></a></td>").
                                                        append("</tr>").
                                                    append("</table>").
                                                append("</td>").
                                            append("</tr>");
                            }
                            
                            htmlContent.append("<tr>").
                                            append("<td valign='top'>").
                                                append("HASIL PEMERIKSAAN").
                                                append("<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                    append("<tr>").
                                                        append("<td width='20%' valign='top'>Situs</td><td width='80%' valign='top'>: ").append(rs2.getString("situs")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='20%' valign='top'>AV-VA</td><td width='80%' valign='top'>: ").append(rs2.getString("av_va")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='20%' valign='top'>Drainase Vena Pulmonalis</td><td width='80%' valign='top'>: ").append(rs2.getString("drainase_vena_pulmonalis")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td valign='top' colspan='2'>").
                                                            append("Katup :").
                                                            append("<table width='99%' align='right' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                                append("<tr>").
                                                                    append("<td width='10%' valign='top'>Mitral</td><td width='90%' valign='top'>: ").append(rs2.getString("katup_mitral")).append("</td>").
                                                                append("</tr>").
                                                                append("<tr>").
                                                                    append("<td width='10%' valign='top'>Aorta</td><td width='90%' valign='top'>: ").append(rs2.getString("katup_aorta")).append("</td>").
                                                                append("</tr>").
                                                                append("<tr>").
                                                                    append("<td width='10%' valign='top'>Tricuspid</td><td width='90%' valign='top'>: ").append(rs2.getString("katup_tricuspid")).append("</td>").
                                                                append("</tr>").
                                                                append("<tr>").
                                                                    append("<td width='10%' valign='top'>Pulmonal</td><td width='90%' valign='top'>: ").append(rs2.getString("katup_pulmonal")).append("</td>").
                                                                append("</tr>").
                                                                append("<tr>").
                                                                    append("<td valign='top' colspan='2'>").
                                                                        append("Keterangan Lain :").
                                                                        append("<table width='99%' align='right' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                                            append("<tr>").
                                                                                append("<td width='15%' border='0' valign='top'>- Septum Atrium</td><td width='85%' border='0' valign='top'>: ").append(rs2.getString("katup_septum_atrium")).append("</td>").
                                                                            append("</tr>").
                                                                            append("<tr>").
                                                                                append("<td width='15%' border='0' valign='top'>- Septum Ventrikal</td><td width='85%' border='0' valign='top'>: ").append(rs2.getString("katup_septum_ventrikal")).append("</td>").
                                                                            append("</tr>").
                                                                            append("<tr>").
                                                                                append("<td width='15%' border='0' valign='top'>- Arkus Aorta</td><td width='85%' border='0' valign='top'>: ").append(rs2.getString("katup_arkus_aorta")).append("</td>").
                                                                            append("</tr>").
                                                                            append("<tr>").
                                                                                append("<td width='15%' border='0' valign='top'>- Lain-lain</td><td width='85%' border='0' valign='top'>: ").append(rs2.getString("katup_keterangan_lainnya")).append("</td>").
                                                                            append("</tr>").
                                                                        append("</table>").
                                                                    append("</td>").
                                                                append("</tr>").
                                                            append("</table>").
                                                        append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='20%' valign='top'>Ruang Jantung</td><td width='80%' valign='top'>: ").append(rs2.getString("ruang_jantung")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td valign='top' colspan='2'>").
                                                            append("M. Mode :").
                                                            append("<table width='99%' align='right' border='0' align='center' cellpadding='3px' cellspacing='0px' class='tbl_form'>").
                                                                append("<tr>").
                                                                    append("<td width='50%' valign='top'>IVDS : ").append(rs2.getString("mode_ivds")).append("</td>").
                                                                    append("<td width='50%' valign='top'>LVPW dextra : ").append(rs2.getString("mode_lvpw_dextra")).append("</td>").
                                                                append("</tr>").
                                                                append("<tr>").
                                                                    append("<td width='50%' valign='top'>IVSS : ").append(rs2.getString("mode_ivss")).append("</td>").
                                                                    append("<td width='50%' valign='top'>LVPW sinistra : ").append(rs2.getString("mode_lvpw_sinistra")).append("</td>").
                                                                append("</tr>").
                                                                append("<tr>").
                                                                    append("<td width='50%' valign='top'>LVID dextra : ").append(rs2.getString("mode_lvid_dextra")).append("</td>").
                                                                    append("<td width='50%' valign='top'>Ejection Fraction : ").append(rs2.getString("mode_ejection_fraction")).append("</td>").
                                                                append("</tr>").
                                                                append("<tr>").
                                                                    append("<td width='50%' valign='top'>LVID sinistra : ").append(rs2.getString("mode_lvid_sinistra")).append("</td>").
                                                                    append("<td width='50%' valign='top'>Fraction Shotening : ").append(rs2.getString("mode_fraction_shotening")).append("</td>").
                                                                append("</tr>").
                                                            append("</table>").
                                                        append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='20%' valign='top'>Doppler</td><td width='80%' valign='top'>: ").append(rs2.getString("doppler")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='20%' valign='top'>Kesimpulan</td><td width='80%' valign='top'>: ").append(rs2.getString("kesimpulan")).append("</td>").
                                                    append("</tr>").
                                                    append("<tr>").
                                                        append("<td width='20%' valign='top'>Saran</td><td width='80%' valign='top'>: ").append(rs2.getString("saran")).append("</td>").
                                                    append("</tr>").
                                                append("</table>").
                                            append("</td>").
                                        append("</tr>");
                        }while(rs2.next());
                        htmlContent.append("</table>").
                                    append("</td>").
                                    append("</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Hasil Pemeriksaan ECHO Pediatrik : "+e);
        }
    }

    private void runBackground(Runnable task) {
        if (ceksukses) return;
        if (executor.isShutdown() || executor.isTerminated()) return;
        if (!isDisplayable()) return;

        ceksukses = true;
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            executor.submit(() -> {
                try {
                    task.run();
                } finally {
                    ceksukses = false;
                    SwingUtilities.invokeLater(() -> {
                        if (isDisplayable()) {
                            setCursor(Cursor.getDefaultCursor());
                        }
                    });
                }
            });
        } catch (RejectedExecutionException ex) {
            ceksukses = false;
        }
    }
    
    @Override
    public void dispose() {
        executor.shutdownNow();
        super.dispose();
    }
}

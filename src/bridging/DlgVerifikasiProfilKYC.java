package bridging;

import fungsi.akses;
import fungsi.batasInput;
import fungsi.koneksiDB;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 * Dialog Verifikasi Profil Pasien - KYC SATUSEHAT
 */
public final class DlgVerifikasiProfilKYC extends javax.swing.JDialog {

    private final SatuSehatKYC kyc = new SatuSehatKYC();
    private String nikPetugas = "";
    private String namapegawai = "";


    public DlgVerifikasiProfilKYC(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadPetugas();
    }

    public DlgVerifikasiProfilKYC(java.awt.Frame parent, boolean modal,
                                   String nikPasien, String namaPasien) {
        super(parent, modal);
        initComponents();
        loadPetugas();
        tNikPasien.setText(nikPasien);
        tNamaPasien.setText(namaPasien);
    }

    /** Ambil nama dan no_ktp petugas login dari tabel pegawai. */
    private void loadPetugas() {
        try (PreparedStatement ps = koneksiDB.condb().prepareStatement(
            "SELECT nama, no_ktp FROM pegawai WHERE nik = ?")) {
            ps.setString(1, akses.getkode());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // isi nama petugas ke textbox
                    tNamaPetugas.setText(rs.getString("nama"));
                    // isi NIK petugas untuk generate URL
                    nikPetugas = rs.getString("no_ktp");
                    if (nikPetugas == null) nikPetugas = "";
                }
            }
        } catch (Exception e) {
            System.out.println("DlgVerifikasiProfilKYC loadPetugas: " + e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        mainFrame = new widget.InternalFrame();
        pnlForm   = new widget.panelisi();
        pnlBtn    = new widget.panelisi();

        lNamaPetugas = new widget.Label();
        lNikPasien   = new widget.Label();
        lNamaPasien  = new widget.Label();
        lStatus      = new widget.Label();

        tNamaPetugas = new widget.TextBox();
        tNikPasien   = new widget.TextBox();
        tNamaPasien  = new widget.TextBox();

        scrStatus = new javax.swing.JScrollPane();
        tStatus   = new javax.swing.JTextArea();

        btnCariPasien = new widget.Button();
        btnBukaUrl    = new widget.Button();
        btnKeluar     = new widget.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        mainFrame.setBorder(javax.swing.BorderFactory.createTitledBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(240, 245, 235)),
                "::[ Verifikasi Profil Pasien - KYC SATUSEHAT ]::",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Tahoma", Font.PLAIN, 11),
                new Color(50, 50, 50)));
        mainFrame.setLayout(new java.awt.BorderLayout(1, 1));

        // ── Form panel ──────────────────────────────────────────────────
        pnlForm.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(3, 5, 3, 5);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        int row = 0;

        lNamaPetugas.setText("Nama Petugas :");
        lNamaPetugas.setPreferredSize(new Dimension(140, 23));
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlForm.add(lNamaPetugas, gbc);
        tNamaPetugas.setEditable(false);
        tNamaPetugas.setPreferredSize(new Dimension(480, 23));
        gbc.gridx = 1; gbc.weightx = 1;
        pnlForm.add(tNamaPetugas, gbc);
        row++;

        lNikPasien.setText("NIK Pasien :");
        lNikPasien.setPreferredSize(new Dimension(140, 23));
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlForm.add(lNikPasien, gbc);
        tNikPasien.setDocument(new batasInput((byte) 16).getKata(tNikPasien));
        tNikPasien.setPreferredSize(new Dimension(480, 23));
        gbc.gridx = 1; gbc.weightx = 1;
        pnlForm.add(tNikPasien, gbc);
        row++;

        lNamaPasien.setText("Nama Pasien :");
        lNamaPasien.setPreferredSize(new Dimension(140, 23));
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlForm.add(lNamaPasien, gbc);
        tNamaPasien.setDocument(new batasInput((byte) 100).getKata(tNamaPasien));
        tNamaPasien.setPreferredSize(new Dimension(480, 23));
        gbc.gridx = 1; gbc.weightx = 1;
        pnlForm.add(tNamaPasien, gbc);
        row++;

        lStatus.setText("Status :");
        lStatus.setPreferredSize(new Dimension(140, 23));
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlForm.add(lStatus, gbc);
        tStatus.setColumns(40);
        tStatus.setRows(3);
        tStatus.setEditable(false);
        tStatus.setFont(new Font("Tahoma", Font.PLAIN, 10));
        tStatus.setForeground(Color.DARK_GRAY);
        tStatus.setLineWrap(true);
        scrStatus.setViewportView(tStatus);
        scrStatus.setPreferredSize(new Dimension(480, 60));
        gbc.gridx = 1; gbc.weightx = 1;
        pnlForm.add(scrStatus, gbc);

        mainFrame.add(pnlForm, java.awt.BorderLayout.CENTER);

        // ── Button panel ────────────────────────────────────────────────
        pnlBtn.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 5));
        pnlBtn.setPreferredSize(new Dimension(660, 40));

        btnCariPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png")));
        btnCariPasien.setMnemonic('C');
        btnCariPasien.setText("Cari Pasien");
        btnCariPasien.setToolTipText("Alt+C");
        btnCariPasien.setPreferredSize(new Dimension(130, 30));
        btnCariPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariPasien();
            }
        });
        pnlBtn.add(btnCariPasien);

        btnBukaUrl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png")));
        btnBukaUrl.setMnemonic('U');
        btnBukaUrl.setText("Buka URL Verifikasi");
        btnBukaUrl.setToolTipText("Alt+U");
        btnBukaUrl.setPreferredSize(new Dimension(160, 30));
        btnBukaUrl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bukaUrlVerifikasi();
            }
        });
        pnlBtn.add(btnBukaUrl);

        btnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png")));
        btnKeluar.setMnemonic('K');
        btnKeluar.setText("Keluar");
        btnKeluar.setToolTipText("Alt+K");
        btnKeluar.setPreferredSize(new Dimension(100, 30));
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });
        pnlBtn.add(btnKeluar);

        mainFrame.add(pnlBtn, java.awt.BorderLayout.PAGE_END);

        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(mainFrame, java.awt.BorderLayout.CENTER);

        setSize(660, 300);
        setLocation(10, 2);
    }// </editor-fold>

    private void cariPasien() {
        if (tNikPasien.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Maaf, silahkan isi NIK Pasien terlebih dahulu.",
                    "Perhatian", JOptionPane.WARNING_MESSAGE);
            tNikPasien.requestFocus();
            return;
        }
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setStatus("Mencari data pasien di SATUSEHAT...");
        try {
            SatuSehatCekNIK cek = new SatuSehatCekNIK();
            String ihsId = cek.tampilIDPasien(tNikPasien.getText().trim());
            if (!ihsId.isEmpty()) {
                if (!cek.name.isEmpty()) tNamaPasien.setText(cek.name);
                setStatus("Pasien ditemukan. Nomor IHS: " + ihsId);
            } else {
                setStatus("Pasien dengan NIK tersebut tidak ditemukan di SATUSEHAT.");
            }
        } catch (Exception e) {
            setStatus("Error: " + e.getMessage());
        } finally {
            this.setCursor(Cursor.getDefaultCursor());
        }
    }
    
    private void bukaUrlVerifikasi() {
        if (nikPetugas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "NIK petugas tidak ditemukan.\nPastikan data no_ktp pegawai sudah diisi.",
                    "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setStatus("Membuat URL verifikasi KYC...");
        try {
            boolean ok = kyc.generateValidationUrl(
                    tNamaPetugas.getText().trim(),
                    nikPetugas);
            if (ok) {
                setStatus("URL verifikasi berhasil dibuat. Membuka browser...");
                try {
                    // Gunakan Desktop API agar cross-platform
                    if (java.awt.Desktop.isDesktopSupported()) {
                        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                        desktop.browse(new java.net.URI(kyc.generateUrl));
                    } else {
                        // fallback jika Desktop tidak tersedia
                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + kyc.generateUrl);
                    }
                    dispose();
                } catch (Exception ex) {
                    setStatus("Gagal membuka browser. URL: " + kyc.generateUrl);
                }
            } else {
                setStatus("Gagal: " + kyc.errorMessage);
                JOptionPane.showMessageDialog(this,
                        "Gagal membuat URL verifikasi.\n" + kyc.errorMessage,
                        "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            setStatus("Error: " + e.getMessage());
            System.out.println("DlgVerifikasiProfilKYC bukaUrlVerifikasi: " + e);
        } finally {
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

    private void setStatus(String msg) {
        tStatus.setText(msg);
        System.out.println("[KYC] " + msg);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgVerifikasiProfilKYC dialog = new DlgVerifikasiProfilKYC(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private widget.InternalFrame mainFrame;
    private widget.panelisi pnlForm;
    private widget.panelisi pnlBtn;
    private widget.Label lNamaPetugas, lNikPasien, lNamaPasien, lStatus;
    private widget.TextBox tNamaPetugas, tNikPasien, tNamaPasien;
    private javax.swing.JScrollPane scrStatus;
    private javax.swing.JTextArea tStatus;
    private widget.Button btnCariPasien, btnBukaUrl, btnKeluar;
}

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Tugas5_Antrian extends JFrame {

    // ─── Input Lambda ───
    private JTextField fieldLambdaDatang;
    private JTextField fieldLambdaLayan;

    // ─── Tombol ───
    private JButton tombolGenerate;
    private JButton tombolResetData;

    // ─── Label Status ───
    private JLabel labelStatusMini;

    // ─── Tabel 1: Data Awal ───
    private JTable tabelDataAwal;
    private DefaultTableModel modelDataAwal;

    // ─── Tabel 2: Hasil FCFS ───
    private JTable tabelHasilFCFS;
    private DefaultTableModel modelHasilFCFS;

    // ─── Tabel 3: Event Oriented ───
    private JTable tabelEventMini;
    private DefaultTableModel modelEventMini;

    // ─── Logic Engine ───
    private MesinAntrianMini mesinKasir;

    // =====================================================
    // Constructor: Rakitin semua komponen GUI
    // =====================================================
    public Tugas5_Antrian() {
        super("Simulasi Antrian FCFS - Kasir Supermarket | Tugas 5");
        mesinKasir = new MesinAntrianMini();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        // Tambahin semua panel
        add(buatPanelKontrol(), BorderLayout.NORTH);
        add(buatPanelTengah(),  BorderLayout.CENTER);

        setVisible(true);
    }

    // =====================================================
    // Panel atas: input + tombol + label status
    // =====================================================
    private JPanel buatPanelKontrol() {
        JPanel panelKontrol = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        panelKontrol.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Parameter Simulasi",
            TitledBorder.LEFT,
            TitledBorder.TOP
        ));
        panelKontrol.setBackground(new Color(240, 248, 255));

        // ── Input Lambda Kedatangan ──
        JLabel lblDatang = new JLabel("λ Kedatangan (cust/menit):");
        fieldLambdaDatang = new JTextField("0.0", 6);
        fieldLambdaDatang.setToolTipText("Rata-rata kedatangan pelanggan per menit");

        // ── Input Lambda Pelayanan ──
        JLabel lblLayan = new JLabel("  λ Pelayanan (cust/menit):");
        fieldLambdaLayan = new JTextField("0.0", 6);
        fieldLambdaLayan.setToolTipText("Rata-rata pelayanan pelanggan per menit");

        // ── Tombol ──
        tombolGenerate  = new JButton("▶  Generate Simulasi");
        tombolResetData = new JButton("↺  Reset Data");

        tombolGenerate.setBackground(new Color(70, 130, 180));
        tombolGenerate.setForeground(Color.BLACK);
        tombolGenerate.setFocusPainted(false);

        tombolResetData.setBackground(new Color(178, 34, 34));
        tombolResetData.setForeground(Color.BLACK);
        tombolResetData.setFocusPainted(false);

        // ── Label Status ──
        labelStatusMini = new JLabel("Status: Siap. Masukkan lambda dan klik Generate.");
        labelStatusMini.setForeground(Color.DARK_GRAY);
        labelStatusMini.setFont(new Font("SansSerif", Font.ITALIC, 12));

        // ── Action Listener ──
        tombolGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jalankanSimulasi();
            }
        });

        tombolResetData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetSemuaData();
            }
        });

        // Rakit ke panel
        panelKontrol.add(lblDatang);
        panelKontrol.add(fieldLambdaDatang);
        panelKontrol.add(lblLayan);
        panelKontrol.add(fieldLambdaLayan);
        panelKontrol.add(tombolGenerate);
        panelKontrol.add(tombolResetData);
        panelKontrol.add(labelStatusMini);

        return panelKontrol;
    }

    // =====================================================
    // Panel tengah: 3 JTable pakai JSplitPane / BoxLayout
    // =====================================================
    private JPanel buatPanelTengah() {
        JPanel panelTengah = new JPanel();
        panelTengah.setLayout(new BoxLayout(panelTengah, BoxLayout.Y_AXIS));
        panelTengah.setBorder(BorderFactory.createEmptyBorder(5, 8, 8, 8));

        // ── Tabel 1 ──
        String[] kolomDataAwal = {
            "Pelanggan ke", "Waktu Antar Kedatangan", "Waktu Datang", "Waktu Pelayanan"
        };
        modelDataAwal = new DefaultTableModel(kolomDataAwal, 0);
        tabelDataAwal = buatJTable(modelDataAwal);

        JScrollPane scrollTabel1 = new JScrollPane(tabelDataAwal);
        scrollTabel1.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
            "Tabel 1 — Data Awal Pelanggan (Generated)",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 12),
            new Color(70, 130, 180)
        ));
        scrollTabel1.setPreferredSize(new Dimension(980, 220));
        scrollTabel1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        // ── Tabel 2 ──
        String[] kolomFCFS = {
            "Pelanggan ke", "Waktu Datang", "Waktu Pelayanan",
            "Waktu Keluar", "Waktu Tunggu", "Waktu di Supermarket"
        };
        modelHasilFCFS = new DefaultTableModel(kolomFCFS, 0);
        tabelHasilFCFS = buatJTable(modelHasilFCFS);

        JScrollPane scrollTabel2 = new JScrollPane(tabelHasilFCFS);
        scrollTabel2.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 1),
            "Tabel 2 — Hasil Antrian FCFS (Total & Rata-rata di baris terakhir)",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 12),
            new Color(34, 139, 34)
        ));
        scrollTabel2.setPreferredSize(new Dimension(980, 230));
        scrollTabel2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));

        // ── Tabel 3 ──
        String[] kolomEvent = {
            "Waktu Event", "Pelanggan ke", "Jenis Event",
            "Jumlah Antrian", "Jumlah di Supermarket", "Status Kasir"
        };
        modelEventMini = new DefaultTableModel(kolomEvent, 0);
        tabelEventMini = buatJTable(modelEventMini);

        JScrollPane scrollTabel3 = new JScrollPane(tabelEventMini);
        scrollTabel3.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(160, 82, 45), 1),
            "Tabel 3 — Event Oriented Simulation (Sorted by Waktu)",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 12),
            new Color(160, 82, 45)
        ));
        scrollTabel3.setPreferredSize(new Dimension(980, 230));
        scrollTabel3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));

        // Masukin semua ke panel tengah
        panelTengah.add(scrollTabel1);
        panelTengah.add(Box.createVerticalStrut(6));
        panelTengah.add(scrollTabel2);
        panelTengah.add(Box.createVerticalStrut(6));
        panelTengah.add(scrollTabel3);

        return panelTengah;
    }

    // =====================================================
    // Helper: bikin JTable yang seragam stylenya
    // =====================================================
    private JTable buatJTable(DefaultTableModel model) {
        JTable tabel = new JTable(model) {
            // Biar cell ga bisa diedit langsung
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabel.setRowHeight(24);
        tabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tabel.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabel.getTableHeader().setBackground(new Color(230, 230, 250));
        tabel.setSelectionBackground(new Color(173, 216, 230));
        tabel.setGridColor(new Color(200, 200, 200));
        tabel.setShowGrid(true);
        tabel.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        return tabel;
    }

    // =====================================================
    // Fungsi utama: validasi input → generate → tampilkan
    // =====================================================
    private void jalankanSimulasi() {
        double lambdaDatang, lambdaLayan;

        // Validasi input dulu, jangan sampe crash gara-gara huruf nyasar
        try {
            lambdaDatang = Double.parseDouble(fieldLambdaDatang.getText().trim());
            lambdaLayan  = Double.parseDouble(fieldLambdaLayan.getText().trim());

            if (lambdaDatang <= 0 || lambdaLayan <= 0) {
                throw new NumberFormatException("Lambda harus lebih dari 0");
            }

        } catch (NumberFormatException ex) {
            labelStatusMini.setText("⚠ Error: Input lambda tidak valid! Masukkan angka positif.");
            labelStatusMini.setForeground(Color.RED);
            fieldLambdaDatang.setBackground(new Color(255, 220, 220));
            fieldLambdaLayan.setBackground(new Color(255, 220, 220));
            return;
        }

        // Reset warna field kalau sebelumnya error
        fieldLambdaDatang.setBackground(Color.WHITE);
        fieldLambdaLayan.setBackground(Color.WHITE);

        // Bersihkan tabel lama dulu sebelum isi yang baru
        modelDataAwal.setRowCount(0);
        modelHasilFCFS.setRowCount(0);
        modelEventMini.setRowCount(0);

        labelStatusMini.setText("Status: Sedang generate data simulasi...");
        labelStatusMini.setForeground(new Color(200, 120, 0));

        // ── Generate Tabel 1: Data Awal ──
        ArrayList<DataPelangganMini> tampungCustMini =
            mesinKasir.generateDataAwalMini(lambdaDatang, lambdaLayan);

        for (DataPelangganMini cust : tampungCustMini) {
            modelDataAwal.addRow(new Object[]{
                cust.nomorUrut,
                String.format("%.2f", cust.jedaDatangMini),
                String.format("%.2f", cust.waktuDatangMini),
                String.format("%.2f", cust.durasiLayanBoss)
            });
        }

        // ── Generate Tabel 2: Hasil FCFS ──
        Object[][] hasilFCFS = mesinKasir.hitungFCFSMini(tampungCustMini);

        for (Object[] baris : hasilFCFS) {
            modelHasilFCFS.addRow(baris);
        }

        // ── Generate Tabel 3: Event Oriented ──
        ArrayList<DataEventMini> hasilEventGabutKasir =
            mesinKasir.buatSimulasiEventMini(tampungCustMini);

        for (DataEventMini ev : hasilEventGabutKasir) {
            String idTampil = (ev.idCustEvent == 0) ? "-" : String.valueOf(ev.idCustEvent);
            modelEventMini.addRow(new Object[]{
                String.format("%.2f", ev.waktuEventMini),
                idTampil,
                ev.jenisEventMini,
                ev.jmlAntriSekarang,
                ev.jmlDiTokoSekarang,
                ev.statusKasirMini
            });
        }

        // Warnain baris "Sibuk" di tabel event biar keliatan
        // (done via renderer sederhana di tampilan default, cukup teks aja)

        labelStatusMini.setText("✔ Selesai! Simulasi 10 pelanggan berhasil di-generate.");
        labelStatusMini.setForeground(new Color(34, 139, 34));
    }

    // =====================================================
    // Reset semua data dan bersihkan tabel
    // =====================================================
    private void resetSemuaData() {
        modelDataAwal.setRowCount(0);
        modelHasilFCFS.setRowCount(0);
        modelEventMini.setRowCount(0);

        fieldLambdaDatang.setText("0.0");
        fieldLambdaLayan.setText("0.0");
        fieldLambdaDatang.setBackground(Color.WHITE);
        fieldLambdaLayan.setBackground(Color.WHITE);

        labelStatusMini.setText("Status: Data direset. Siap simulasi baru.");
        labelStatusMini.setForeground(Color.DARK_GRAY);
    }

    // =====================================================
    // Entry point — jalanin dari sini
    // =====================================================
    public static void main(String[] args) {
        // Biar tampilan ngikutin sistem (Windows/Mac/Linux)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // kalau gagal ya pakai default aja, santai
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Tugas5_Antrian();
            }
        });
    }
}
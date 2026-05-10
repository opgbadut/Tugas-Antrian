import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MesinAntrianMini {

    // Jumlah pelanggan yang disimulasikan, fix 10 orang
    private static final int TOTAL_CUST = 10;

    // ===================================================
    // TABEL 1: Generate data awal pelanggan
    // Pakai distribusi eksponensial: -ln(1-U) / lambda
    // ===================================================
    public ArrayList<DataPelangganMini> generateDataAwalMini(double lambdaDatang, double lambdaLayan) {
        ArrayList<DataPelangganMini> tampungCustMini = new ArrayList<>();

        double akumWaktuDatang = 0.0; // Akumulasi waktu kedatangan

        for (int i = 1; i <= TOTAL_CUST; i++) {

            // Generate poisson manual dulu coy
            double acakJeda  = Math.random();
            double acakLayan = Math.random();

            // Rumus eksponensial buat waktu antar kedatangan
            double jedaMenit = -Math.log(1 - acakJeda) / lambdaDatang;

            // Waktu datang = kumulatif jeda sebelumnya
            akumWaktuDatang += jedaMenit;

            // Generate durasi pelayanan pakai rumus yang sama
            double layanMenit = -Math.log(1 - acakLayan) / lambdaLayan;

            // Masuk ke list
            tampungCustMini.add(new DataPelangganMini(i, jedaMenit, akumWaktuDatang, layanMenit));
        }

        return tampungCustMini;
    }

    // ===================================================
    // TABEL 2: Hitung hasil antrian FCFS
    // Setiap pelanggan nunggu kalau kasir masih sibuk
    // Return: array 2D buat langsung masuk JTable
    // ===================================================
    public Object[][] hitungFCFSMini(ArrayList<DataPelangganMini> tampungCustMini) {
        // Hasil nanti ditampung sini
        Object[][] hasilFCFS = new Object[TOTAL_CUST + 1][6]; // +1 buat baris total/rata2

        double waktuCabutCust     = 0.0; // Kapan kasir selesai pelanggan sebelumnya
        double totalNgaretKasir   = 0.0; // Akum waktu tunggu semua pelanggan
        double totalDurasiDiToko  = 0.0; // Akum waktu di supermarket semua

        for (int i = 0; i < TOTAL_CUST; i++) {
            DataPelangganMini cust = tampungCustMini.get(i);

            // Kasir masih sibuk jadi ngantri bentar, atau langsung dilayani
            double waktuMulaiLayan = Math.max(cust.waktuDatangMini, waktuCabutCust);

            double waktuKeluar   = waktuMulaiLayan + cust.durasiLayanBoss;
            double waktuNunggu   = Math.max(0, waktuCabutCust - cust.waktuDatangMini);
            double waktuDiToko   = waktuKeluar - cust.waktuDatangMini;

            totalNgaretKasir  += waktuNunggu;
            totalDurasiDiToko += waktuDiToko;

            hasilFCFS[i][0] = cust.nomorUrut;
            hasilFCFS[i][1] = String.format("%.2f", cust.waktuDatangMini);
            hasilFCFS[i][2] = String.format("%.2f", cust.durasiLayanBoss);
            hasilFCFS[i][3] = String.format("%.2f", waktuKeluar);
            hasilFCFS[i][4] = String.format("%.2f", waktuNunggu);
            hasilFCFS[i][5] = String.format("%.2f", waktuDiToko);

            // Update tracker waktu kasir selesai
            waktuCabutCust = waktuKeluar;
        }

        // Baris terakhir: total & rata-rata
        hasilFCFS[TOTAL_CUST][0] = "TOTAL / RATA2";
        hasilFCFS[TOTAL_CUST][1] = "-";
        hasilFCFS[TOTAL_CUST][2] = "-";
        hasilFCFS[TOTAL_CUST][3] = "-";
        hasilFCFS[TOTAL_CUST][4] = String.format("%.2f / %.2f", totalNgaretKasir, totalNgaretKasir / TOTAL_CUST);
        hasilFCFS[TOTAL_CUST][5] = String.format("%.2f / %.2f", totalDurasiDiToko, totalDurasiDiToko / TOTAL_CUST);

        return hasilFCFS;
    }

    // ===================================================
    // TABEL 3: Event Oriented Simulation
    // Bikin dua event per pelanggan (Datang & Keluar)
    // Terus di-sort dan diproses satu-satu
    // ===================================================
    public ArrayList<DataEventMini> buatSimulasiEventMini(ArrayList<DataPelangganMini> tampungCustMini) {

        // Tampung semua event dulu sebelum diproses
        ArrayList<DataEventMini> bufferAntrianMini = new ArrayList<>();

        // Event awal: sistem nyala di waktu 0
        bufferAntrianMini.add(new DataEventMini(0.0, 0, "Mulai"));

        // Hitung dulu waktu keluar pakai FCFS biar kita tau kapan tiap pelanggan cabut
        double waktuCabutTracker = 0.0;
        double[] arrWaktuKeluar  = new double[TOTAL_CUST]; // simpan waktu keluar tiap cust

        for (int i = 0; i < tampungCustMini.size(); i++) {
            DataPelangganMini cust = tampungCustMini.get(i);
            double mulaiLayan      = Math.max(cust.waktuDatangMini, waktuCabutTracker);
            arrWaktuKeluar[i]      = mulaiLayan + cust.durasiLayanBoss;
            waktuCabutTracker      = arrWaktuKeluar[i];
        }

        // Sekarang pecah jadi event Datang & Keluar
        for (int i = 0; i < tampungCustMini.size(); i++) {
            DataPelangganMini cust = tampungCustMini.get(i);

            // Event kedatangan
            bufferAntrianMini.add(new DataEventMini(cust.waktuDatangMini, cust.nomorUrut, "Datang"));

            // Event keluar (pakai waktu yang udah dihitung FCFS)
            bufferAntrianMini.add(new DataEventMini(arrWaktuKeluar[i], cust.nomorUrut, "Keluar"));
        }

        // Sorting event biar timeline nggak rusak
        // Kalau waktu sama: keluar dulu baru datang (biar kasir kebagian)
        Collections.sort(bufferAntrianMini, new Comparator<DataEventMini>() {
            @Override
            public int compare(DataEventMini ev1, DataEventMini ev2) {
                if (ev1.waktuEventMini != ev2.waktuEventMini) {
                    return Double.compare(ev1.waktuEventMini, ev2.waktuEventMini);
                }
                // Waktu sama: Keluar dulu, baru Datang
                if (ev1.jenisEventMini.equals("Keluar") && ev2.jenisEventMini.equals("Datang")) return -1;
                if (ev1.jenisEventMini.equals("Datang") && ev2.jenisEventMini.equals("Keluar")) return  1;
                return Integer.compare(ev1.idCustEvent, ev2.idCustEvent);
            }
        });

        // Proses satu-satu, update state system
        int    jmlDiAntrian    = 0;
        int    jmlDiToko       = 0;
        boolean kasirLagiBoss  = false; // false = menganggur, true = sibuk

        ArrayList<DataEventMini> hasilEventGabutKasir = new ArrayList<>();

        for (DataEventMini ev : bufferAntrianMini) {

            if (ev.jenisEventMini.equals("Mulai")) {
                // Reset semua, kasir masih nganggur
                jmlDiAntrian  = 0;
                jmlDiToko     = 0;
                kasirLagiBoss = false;

            } else if (ev.jenisEventMini.equals("Datang")) {
                jmlDiToko++;
                if (kasirLagiBoss) {
                    // Kasir masih sibuk jadi masuk antrian
                    jmlDiAntrian++;
                } else {
                    // Kasir nganggur, langsung dilayani
                    kasirLagiBoss = true;
                }

            } else if (ev.jenisEventMini.equals("Keluar")) {
                jmlDiToko--;
                if (jmlDiAntrian > 0) {
                    // Masih ada yang ngantri, kasir tetap sibuk
                    jmlDiAntrian--;
                } else {
                    // Awas index jangan sampe jebol, antrian udah kosong
                    kasirLagiBoss = false;
                }
            }

            // Simpan state ke event object
            ev.jmlAntriSekarang  = jmlDiAntrian;
            ev.jmlDiTokoSekarang = jmlDiToko;
            ev.statusKasirMini   = kasirLagiBoss ? "Sibuk" : "Menganggur";

            hasilEventGabutKasir.add(ev);
        }

        return hasilEventGabutKasir;
    }
}
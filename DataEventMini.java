// =====================================================
// DataEventMini.java
// Class buat nampung satu event di timeline simulasi
// Event bisa: Datang, Keluar, atau Mulai
// =====================================================

public class DataEventMini {

    // Kapan event ini terjadi (dalam satuan menit)
    double waktuEventMini;

    // Pelanggan keberapa yang trigger event ini
    // 0 = event awal sistem (bukan pelanggan beneran)
    int idCustEvent;

    // Tipe event: "Datang", "Keluar", atau "Mulai"
    String jenisEventMini;

    // Berapa orang yang lagi ngantri pas event ini terjadi
    int jmlAntriSekarang;

    // Berapa total orang yang ada di supermarket (ngantri + dilayani)
    int jmlDiTokoSekarang;

    // Status kasir pas event ini: "Sibuk" atau "Menganggur"
    String statusKasirMini;

    // Constructor event
    public DataEventMini(double waktuEventMini, int idCustEvent, String jenisEventMini) {
        this.waktuEventMini  = waktuEventMini;
        this.idCustEvent     = idCustEvent;
        this.jenisEventMini  = jenisEventMini;
        // field antrian & status diisi nanti pas proses event
        this.jmlAntriSekarang  = 0;
        this.jmlDiTokoSekarang = 0;
        this.statusKasirMini   = "Menganggur";
    }
}
public class DataPelangganMini {

    // Urutan pelanggan keberapa (1-10)
    int nomorUrut;

    // Waktu antar kedatangan dari pelanggan sebelumnya (menit)
    double jedaDatangMini;

    // Waktu pelanggan ini nyampe ke kasir
    double waktuDatangMini;

    // Durasi pelayanan pelanggan ini di kasir
    double durasiLayanBoss;

    // Constructor buat bikin objek pelanggan baru
    public DataPelangganMini(int nomorUrut, double jedaDatangMini, double waktuDatangMini, double durasiLayanBoss) {
        this.nomorUrut       = nomorUrut;
        this.jedaDatangMini  = jedaDatangMini;
        this.waktuDatangMini = waktuDatangMini;
        this.durasiLayanBoss = durasiLayanBoss;
    }
}
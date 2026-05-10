# 🛒 Simulasi Antrian — Kasir
 
Simulasi antrian kasir menggunakan metode **First Come First Serve (FCFS)** berbasis Java Desktop GUI (Swing).

---

## 📋 Deskripsi

Program ini mensimulasikan proses antrian pelanggan di kasir dengan satu server (kasir). Data kedatangan dan pelayanan di-generate secara acak menggunakan **distribusi eksponensial**. Program menampilkan tiga tabel hasil simulasi secara langsung setelah tombol Generate ditekan.

---

## 🗂️ Struktur File

```
📁 project/
├── Tugas5_Antrian.java       # GUI utama (JFrame, tombol, tabel)
├── MesinAntrianMini.java     # Logic FCFS + Event Oriented Simulation
├── DataPelangganMini.java    # Class object data pelanggan
└── DataEventMini.java        # Class object event simulasi
```

> Semua file berada dalam **satu folder yang sama**, tidak menggunakan package.

---

## ✨ Fitur

- Generate otomatis data **10 pelanggan** dengan distribusi eksponensial
- Input parameter **λ kedatangan** dan **λ pelayanan** yang bisa diubah
- **Tabel 1** — Data awal: waktu antar kedatangan, waktu datang, waktu pelayanan
- **Tabel 2** — Hasil antrian FCFS: waktu keluar, waktu tunggu, waktu di supermarket + total & rata-rata
- **Tabel 3** — Event Oriented Simulation: timeline event sorted by waktu, status kasir, jumlah antrian
- Label status berwarna **hijau** saat berhasil, **merah** saat input tidak valid

---

## ⚙️ Teknologi

- Java SE (JDK 8+)
- Java Swing
- Tanpa library eksternal, Maven, atau Gradle

---

## 🚀 Cara Menjalankan

### 1. Pastikan JDK sudah terinstall

```bash
java -version
javac -version
```

### 2. Compile semua file

```bash
javac *.java
```

### 3. Jalankan program

```bash
java Tugas5_Antrian
```

### Menggunakan IDE (IntelliJ / Eclipse / NetBeans)

1. Buat project Java baru
2. Copy semua file `.java` ke folder `src`
3. Klik kanan `Tugas5_Antrian.java` → **Run**

---

## 🧮 Rumus yang Digunakan

**Generate waktu (distribusi eksponensial):**
```
waktu = -ln(1 - U) / λ
```

**Waktu keluar pelanggan:**
```
waktuKeluar = max(waktuDatang, waktuKeluarSebelumnya) + waktuPelayanan
```

**Waktu tunggu:**
```
waktuTunggu = max(0, waktuKeluarSebelumnya - waktuDatang)
```

**Waktu di supermarket:**
```
waktuDiToko = waktuKeluar - waktuDatang
```

---

## 📌 Catatan

- Jika ada event **Datang** dan **Keluar** di waktu yang sama, event **Keluar diproses lebih dulu** agar kasir sempat dibebaskan sebelum menerima pelanggan berikutnya.
- File `.class` yang muncul banyak (misal `Tugas5_Antrian$1.class`) adalah hasil compile anonymous class — **normal**, tidak perlu diubah.

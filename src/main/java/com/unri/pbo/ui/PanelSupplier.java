/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.unri.pbo.ui;
import com.unri.pbo.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Strgirlezz
 */
public class PanelSupplier extends javax.swing.JPanel {

    /**
     * Creates new form PanelSupplier
     */
public PanelSupplier() {
        initComponents();
        jTextField1.setEditable(false);
        jTextField1.setBackground(java.awt.Color.WHITE);
        tampilData();
        generateIdSupplier();

        // Klik baris tabel -> pindahkan data ke form input
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = jTable1.getSelectedRow();
                if (row != -1) {
                    jTextField1.setText(jTable1.getValueAt(row, 0).toString());
                    jTextField2.setText(jTable1.getValueAt(row, 1).toString());
                    jTextField3.setText(jTable1.getValueAt(row, 2) != null ? jTable1.getValueAt(row, 2).toString() : "");
                    jTextField4.setText(jTable1.getValueAt(row, 3) != null ? jTable1.getValueAt(row, 3).toString() : "");
                }
            }
        });
    }

    private void tampilData() {
       DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID Supplier", "Nama Perusahaan", "No.Telepon", "Alamat"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String sql = "SELECT id_supplier, nama_perusahaan, no_telepon, alamat FROM supplier ORDER BY id_supplier ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_supplier"),
                    rs.getString("nama_perusahaan"),
                    rs.getString("no_telepon"),
                    rs.getString("alamat")
                });
            }
            jTable1.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data supplier: " + e.getMessage());
        }
    }

    private void generateIdSupplier() {
        String sql = "SELECT id_supplier FROM supplier ORDER BY id_supplier DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next() && rs.getString("id_supplier") != null) {
                String kodeTerakhir = rs.getString("id_supplier");
                int angka = Integer.parseInt(kodeTerakhir.replaceAll("[^0-9]", "")) + 1;
                jTextField1.setText(String.format("SUP%03d", angka));
            } else {
                jTextField1.setText("SUP001");
            }
        } catch (Exception e) {
            jTextField1.setText("SUP001");
        }
    }

    private void bersihkanForm() {
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTable1.clearSelection();
        generateIdSupplier();
        jTextField2.requestFocus();
    }

    private boolean validasiInput() {
        if (jTextField2.getText().trim().isEmpty() || jTextField3.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Perusahaan dan No.Telepon wajib diisi!");
            return false;
        }
        return true;
    }

    private void simpanDataBaru() {
       String sql = "INSERT INTO supplier (id_supplier, nama, nama_perusahaan, no_telepon, alamat) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, jTextField1.getText().trim());
            ps.setString(2, jTextField2.getText().trim()); // isi kolom nama sama dengan nama perusahaan
            ps.setString(3, jTextField2.getText().trim()); // nama_perusahaan
            ps.setString(4, jTextField3.getText().trim()); // no_telepon
            ps.setString(5, jTextField4.getText().trim()); // alamat

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data supplier berhasil disimpan!");
            tampilData();
            bersihkanForm();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this, "ID Supplier sudah terdaftar!");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + e.getMessage());
            }
        }
    }
        

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setText("ID Supplier");

        jLabel3.setText("Nama Perusahaan");

        jLabel4.setText("No.Telepon");

        jLabel5.setText("Alamat");

        jButton3.setBackground(new java.awt.Color(0, 153, 102));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setText("Simpan");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Reset");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton2.setText("Hapus");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("Tambah");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton5.setText("Ubah");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField3)
                    .addComponent(jTextField1)
                    .addComponent(jTextField2)
                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(170, 170, 170)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(174, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addGap(41, 41, 41)
                .addComponent(jButton3)
                .addContainerGap(132, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID Supplier", "Nama Perusahaan", "No.Telepon", "Alamat"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
   bersihkanForm();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
      String sql = "INSERT INTO supplier (id_supplier, nama_perusahaan, no_telepon, alamat) VALUES (?, ?, ?, ?)";
try (Connection conn = DatabaseConnection.getConnection();
     PreparedStatement ps = conn.prepareStatement(sql)) {

    ps.setString(1, jTextField1.getText().trim());
    ps.setString(2, jTextField2.getText().trim());
    ps.setString(3, jTextField3.getText().trim());
    ps.setString(4, jTextField4.getText().trim());

    ps.executeUpdate();
    JOptionPane.showMessageDialog(this, "Data supplier berhasil disimpan!");
    tampilData();
    bersihkanForm();
} catch (SQLException e) {
    if (e.getErrorCode() == 1062) {
        JOptionPane.showMessageDialog(this, "ID Supplier sudah terdaftar!");
    } else {
        JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + e.getMessage());
    }
}
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    int row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data supplier pada tabel terlebih dahulu!");
            return;
        }

        String id = jTable1.getValueAt(row, 0).toString();
        String nama = jTable1.getValueAt(row, 1).toString();

        int konfirmasi = JOptionPane.showConfirmDialog(
            this,
            "Hapus supplier \"" + nama + "\" (" + id + ")?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION
        );
        if (konfirmasi != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM supplier WHERE kode_supplier = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data supplier berhasil dihapus!");
            tampilData();
            bersihkanForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    bersihkanForm();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    simpanDataBaru();
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}

package tugas;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class transaksi extends javax.swing.JFrame {
    
   
    
    
    
    private void date(){
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        Date date = new Date();
        jLabel4.setText(dateFormat.format(date));
    }
    
   
    
    private void tampildata(){
        String[]baris={"ID Transaksi", "Kode Barang", "Nama Barang", "Jumlah Beli", "Total Harga", "stok"};
        DefaultTableModel tabmode = new DefaultTableModel(null, baris);
        tabeltrx.setModel(tabmode);
        tabeltrx.getModel();
        tabmode.setRowCount(0);
        try{
            String sql = "SELECT tb_dtltransaksi.id_transaksi,tb_dtltransaksi.id_barang,tb_barang.nama_barang,tb_dtltransaksi.jumlah_beli,tb_dtltransaksi.harga,tb_barang.stok FROM tb_dtltransaksi,tb_barang WHERE tb_dtltransaksi.id_transaksi='"+txttransaksi.getText()+"' AND tb_barang.id_barang=tb_dtltransaksi.id_barang";
            //String sql = "SELECT tb_dtltransaksi.id_transaksi,tb_dtltransaksi.id_barang,tb_barang.nama_barang,tb_dtltransaksi.jumlah_beli,tb_dtltransaksi.harga FROM tb_dtltransaksi JOIN tb_barang USING (id_barang) WHERE tb_dtltransaksi.id_transaksi='"+txttransaksi.getText()+"'";
            
            Connection conn = konektor.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);
            int total_harga=0; //deklarasi total harga keseluruhan
            while(res.next()){
                tabmode.addRow(new Object[]{res.getString(1), res.getString(2), res.getString(3), res.getString(4), Integer.parseInt(res.getString(5))*Integer.parseInt(res.getString(4)), res.getString("stok")});
                total_harga+=Integer.parseInt(res.getString(5))*Integer.parseInt(res.getString(4));
//                System.out.println(total_harga);
            }
            tabeltrx.setModel(tabmode);
            tabeltrx.getColumnModel().getColumn(5).setMaxWidth(0);
            tabeltrx.getColumnModel().getColumn(5).setMinWidth(0);
            tabeltrx.getColumnModel().getColumn(5).setWidth(0);
            //tabmode.setRowCount(0);
            txttotalharga.setText(Integer.toString(total_harga));
//            System.out.println(total_harga);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "GAGAL", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void tampilkan(){
        try{
            String sql = "select nama_barang, stok, harga from tb_barang WHERE id_barang='"+barangcmb.getSelectedItem()+"'";
            Connection conn = konektor.configDB();
            Statement stm = conn.createStatement(); 
            ResultSet res = stm.executeQuery(sql);
            
            while(res.next()){
                Object[] data = new Object[3];
                data[0]=res.getString(1);
                data[1]=res.getString(2);
                data[2]=res.getString(3);
                
                txtnamabarang.setText((String) data[0]);
                txtstok.setText((String) data[1]);
                txtharga.setText((String) data[2]);
                
            }
            
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    private void data_cmb(){
        try{
            String sql = "select id_barang from tb_barang";
            Connection conn = konektor.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);
            while (res.next()){
                barangcmb.addItem(res.getString(1));
               
            }
        }catch (Exception e){
        }
    }

    private void harga(){
        try {
            String barangcbx = (String)barangcmb.getSelectedItem();
            String sql = "SELECT * from tb_barang where id_barang='"+barangcbx+"'";
            Connection conn = konektor.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);
  //                                          + "WHERE id_barang='"+barangcbx+"'");
            
            
            if (res.next()){
                int harga = Integer.parseInt(res.getString("harga")) * Integer.parseInt(txtjumlahbeli.getText()); 
                        txttotal.setText("RP" +harga);
                        
                               // System.out.print(txtjumlahbeli.getText());
            }else{
                
            }
                    
        }catch (Exception e){
            System.out.print("error di harga "+ e);
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private void resetall(){
        barangcmb.setSelectedIndex(0);
        //txttransaksi.setText();
        txtnamabarang.setText(null);
        txtstok.setText(null);
        txtharga.setText(null);
        txtjumlahbeli.setText(null);
        txttotal.setText(null);
        tabeltrx.removeAll();
        txtbayar.setText(null);
        txtkembalian.setText(null);
    }
    
    private void reset(){
        barangcmb.setSelectedIndex(0);
        txtnamabarang.setText(null);
        txtstok.setText(null);
        txtharga.setText(null);
        txtjumlahbeli.setText(null);
        txttotal.setText(null);
    }
    
    public void exporexcel(){
        FileWriter filewriter;
        JFileChooser report = new JFileChooser();
        report.setCurrentDirectory(new File("[B]export_output/excel[/B]"));
        int retrival = report.showSaveDialog(null);
        if(retrival == JFileChooser.APPROVE_OPTION){
            try{
                    TableModel tModel = tabeltrx.getModel();
                    filewriter = new FileWriter(new File(report.getSelectedFile() + ".xls"));
                    
                    for (int i=0; i< tabeltrx.getColumnCount(); i++){
                        filewriter.write(tabeltrx.getColumnName(i).toUpperCase() + "\t");
                    }
                    filewriter.write("\n");
                    for(int i=0; i<tabeltrx.getRowCount(); i++){
                    for(int j=0; j<tabeltrx.getColumnCount(); j++){
                        filewriter.write(tabeltrx.getValueAt(i,j).toString() + "\t");
                    }
                    filewriter.write("\n");
                }
                    filewriter.close();
            }catch (Exception e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
        
    }
    
    private void kodeoto(){
    try{
        
        String sql = "select * from tb_transaksi order by id_transaksi desc";
        Connection conn = konektor.configDB();
        Statement stm = conn.createStatement();
        ResultSet res = stm.executeQuery(sql);
        if(res.next()){
            String kodetrx = res.getString(1).substring(1);
            String an = "" + (Integer.parseInt(kodetrx) + 1);
            String nol = "";
            
            if(an.length()==1)
            {nol="000";}
            else if(an.length()==2)
            {nol="00";}
            else if(an.length()==3)
            {nol="0";}
            else if(an.length()==4)
            {nol="";}
            
            txttransaksi.setText("K" + nol + an);
        }else{
            txttransaksi.setText("K0001");
            }
        }catch (SQLException e){
                JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public transaksi() {
        initComponents();
        kodeoto();
        data_cmb();
        tampildata();
        date();
        //exporexcel();
        
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
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txttransaksi = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txttotalharga = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        barangcmb = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        txtnamabarang = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtstok = new javax.swing.JTextField();
        txtjumlahbeli = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txttotal = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtharga = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabeltrx = new javax.swing.JTable();
        tambahbrgbtn = new javax.swing.JButton();
        hapusbrgbtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        simpanbtn = new javax.swing.JButton();
        tutupbtn = new javax.swing.JButton();
        txtbayar = new javax.swing.JTextField();
        txtkembalian = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        ubahbtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TRANSAKSINYA GAN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(102, 255, 255));

        jLabel2.setText("No. Transaksi");

        jLabel3.setText("Tgl Transaksi");

        txttransaksi.setEditable(false);
        txttransaksi.setEnabled(false);
        txttransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttransaksiActionPerformed(evt);
            }
        });

        jLabel10.setText("TOTAL HARGA GAN");

        txttotalharga.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txttransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txttotalharga, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txttransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(22, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(9, 9, 9))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txttotalharga)
                        .addContainerGap())))
        );

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Kode Barang");

        barangcmb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--Pilih Gan--" }));
        barangcmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barangcmbActionPerformed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Nama Barang Gan");

        txtnamabarang.setEditable(false);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Stok Gan");

        txtstok.setEditable(false);
        txtstok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtstokActionPerformed(evt);
            }
        });

        txtjumlahbeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtjumlahbeliActionPerformed(evt);
            }
        });
        txtjumlahbeli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtjumlahbeliKeyPressed(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Total Gan");

        txttotal.setEditable(false);
        txttotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotalActionPerformed(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Beli Berapa ?");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Harga Gan");

        txtharga.setEditable(false);

        tabeltrx.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabeltrx.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabeltrxMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabeltrx);

        tambahbrgbtn.setText("TambahGan");
        tambahbrgbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahbrgbtnActionPerformed(evt);
            }
        });

        hapusbrgbtn.setText("HapusGan");
        hapusbrgbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusbrgbtnActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(51, 255, 255));

        simpanbtn.setText("SimpanGan");
        simpanbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanbtnActionPerformed(evt);
            }
        });

        tutupbtn.setText("TutupGan");
        tutupbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tutupbtnActionPerformed(evt);
            }
        });

        txtbayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtbayarActionPerformed(evt);
            }
        });
        txtbayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtbayarKeyPressed(evt);
            }
        });

        jLabel12.setText("Bayar");

        jLabel13.setText("Kembali");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(simpanbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tutupbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtbayar, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtkembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtbayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtkembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(simpanbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tutupbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        ubahbtn.setText("UbahGan");
        ubahbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubahbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(barangcmb, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnamabarang, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtstok, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(txtharga, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtjumlahbeli, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tambahbrgbtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(hapusbrgbtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ubahbtn)))))
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(barangcmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnamabarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtstok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtjumlahbeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtharga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tambahbrgbtn)
                    .addComponent(hapusbrgbtn)
                    .addComponent(ubahbtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txttransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttransaksiActionPerformed
        
    }//GEN-LAST:event_txttransaksiActionPerformed

    private void barangcmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barangcmbActionPerformed
        tampilkan(); //tampilkan data di ComboBox
    }//GEN-LAST:event_barangcmbActionPerformed

    private void txtjumlahbeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtjumlahbeliActionPerformed
        
    }//GEN-LAST:event_txtjumlahbeliActionPerformed

    private void tambahbrgbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahbrgbtnActionPerformed
       try {
           Connection conn = konektor.configDB();
           //Mengupdate database dtltransaksi dan menampilkan ke tabel
           String updatedata = "SELECT * FROM tb_dtltransaksi WHERE id_transaksi='"+txttransaksi.getText()+"' AND id_barang='"+barangcmb.getSelectedItem()+"'";
           Statement stm = conn.createStatement();
           ResultSet rs = stm.executeQuery(updatedata);
           if(rs.next()){
               //UPDATE Jumlah beli barangnya jika membeli barang dengan kode yang sama maka hanya jumlah beli yg berubah/update
               int stokbaru = rs.getInt("jumlah_beli") + Integer.parseInt(txtjumlahbeli.getText());
               String jumlahbaru = "UPDATE tb_dtltransaksi SET jumlah_beli='"+stokbaru+"' WHERE id_dtltransaksi='"+rs.getString("id_dtltransaksi")+"'";
               PreparedStatement ps = conn.prepareStatement(jumlahbaru);
               ps.execute();
           }else{
               //Memasukan data ke database
        String sql = "INSERT INTO tb_dtltransaksi(id_transaksi,id_barang,nama_barang,harga,jumlah_beli) VALUES ('"+txttransaksi.getText()+"','"+barangcmb.getSelectedItem().toString()+"','"+txtnamabarang.getText()+"','"+txtharga.getText()+"','"+txtjumlahbeli.getText()+"')";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.execute();
           }
           //UPDATE stok berkurang setelah ditambahkan ke database
        
    }catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
            }tampildata(); reset();
    }//GEN-LAST:event_tambahbrgbtnActionPerformed

    private void hapusbrgbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusbrgbtnActionPerformed
        try{
            Connection conn = konektor.configDB();
            
            String sql = "SELECT a.id_dtltransaksi, a.id_barang, b.stok, a.jumlah_beli FROM tb_dtltransaksi a, tb_barang b WHERE a.id_barang=b.id_barang AND a.id_transaksi='"+txttransaksi.getText()+"' AND a.id_barang='"+barangcmb.getSelectedItem()+"'";
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);
            if(res.next()){
                //int updatestok= Integer.parseInt(txtstok.getText()) + Integer.parseInt(txtjumlahbeli.getText());
                int updatestok = res.getInt("stok") + res.getInt("jumlah_beli");
                String stokbaru = "UPDATE tb_barang SET stok='"+updatestok+"' WHERE id_barang='"+res.getString("id_barang")+"'";
                PreparedStatement pst = conn.prepareStatement(stokbaru);
                pst.execute();
            }else{
                
            }
            String hapusdtl = "DELETE FROM tb_dtltransaksi WHERE id_barang='"+barangcmb.getSelectedItem()+"'";
                PreparedStatement psm = conn.prepareStatement(hapusdtl);
                psm.execute();
                JOptionPane.showMessageDialog(null, "Berhasil");
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }tampildata(); reset();
    }//GEN-LAST:event_hapusbrgbtnActionPerformed

    private void txtjumlahbeliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtjumlahbeliKeyPressed
        if(txtjumlahbeli.getText().contains("-") ){ //contains memeriksa apakah terdapat data atau variabel "-" 
            JOptionPane.showMessageDialog(null,"Jumlah yang diminta tidak valid! ");
                       tambahbrgbtn.setEnabled(false); //tombol tambah dinonaktifkan
                       txtjumlahbeli.setText(""); //jika terdapat data atau variabel "-" maka txtjumlah beli di set "" atau kosongan
        }
        else if(!txtjumlahbeli.getText().equals("")){
           harga();
           try {
               String barangcbx = (String)barangcmb.getSelectedItem();
//               Connection conn = konektor.configDB();
//               Statement stm = conn.createStatement();
//               ResultSet res = stm.executeQuery("SELECT id_barang, stok FROM tb_barang WHERE id_barang='"+barangcbx+"'");
               
//               if(res.next()){
                   if(Integer.parseInt(txtjumlahbeli.getText()) > Integer.parseInt(txtstok.getText())){
                       JOptionPane.showMessageDialog(null,"Jumlah melebih stok yang ada! ");
                       tambahbrgbtn.setEnabled(false);
                   //}else if(txtjumlahbeli.getText().contains("-")){
                     //  JOptionPane.showMessageDialog(null,"Jumlah yang diminta tidak valid! ");
                       //tambahbrgbtn.setEnabled(false);
                   }else{
                       tambahbrgbtn.setEnabled(true);
                   }
//                   simpanbtn.setVisible(false);
//               }else{
//                   simpanbtn.setVisible(true);
//               }
//           }else{
//                   }
        }catch (Exception e){
                JOptionPane.showMessageDialog(null,"Error");
                   
               }
           
        }

    }//GEN-LAST:event_txtjumlahbeliKeyPressed

    private void txttotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotalActionPerformed

    private void simpanbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanbtnActionPerformed
        try {
            String sql = "SELECT * FROM tb_dtltransaksi WHERE id_transaksi='"+txttransaksi.getText()+"'";
            Connection conn = konektor.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);
            while(res.next()){
                int update = Integer.parseInt(res.getString("jumlah_beli"));
                String updatestok = "UPDATE tb_barang SET stok=stok - '"+update+"' WHERE id_barang='"+res.getString("id_barang")+"'";
                PreparedStatement psm = conn.prepareStatement(updatestok);
                psm.execute();
            //JOptionPane.showMessageDialog(null, "Berhasil ditambahkan");
                
            }
            String masukdata = "INSERT INTO tb_transaksi(id_transaksi, tgl_transaksi, total_harga) VALUES ('"+txttransaksi.getText()+"', '"+jLabel4.getText()+"','"+txttotalharga.getText()+"')";
            PreparedStatement pst = conn.prepareStatement(masukdata);
            pst.execute();
            
            String masukreport = "INSERT INTO laporan(id_transaksi, tgl_transaksi, total_harga) VALUES ('"+txttransaksi.getText()+"', '"+jLabel4.getText()+"','"+txttotalharga.getText()+"')";
            PreparedStatement pts = conn.prepareStatement(masukreport);
            pts.execute();
            //PreparedStatement pst = conn.prepareStatement(sql);
            //pst.execute();
            JOptionPane.showMessageDialog(null, "Tersimpan");
            
            
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }resetall();  kodeoto(); tampildata();
    }//GEN-LAST:event_simpanbtnActionPerformed

    private void tutupbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tutupbtnActionPerformed
        this.setVisible(false);
        new master().setVisible(true);
    }//GEN-LAST:event_tutupbtnActionPerformed

    private void tabeltrxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabeltrxMouseClicked
         int baris = tabeltrx.rowAtPoint(evt.getPoint());
        String kode=tabeltrx.getValueAt(baris, 1).toString();
        barangcmb.setSelectedItem(kode);
        String nama=tabeltrx.getValueAt(baris, 2).toString();
        txtnamabarang.setText(nama);
        //String harga=tabeltrx.getValueAt(baris, 4).toString();
        //txtharga.setText(harga);
        String jumlah=tabeltrx.getValueAt(baris, 3).toString();
        txtjumlahbeli.setText(jumlah);
        String total=tabeltrx.getValueAt(baris,4).toString();
        txttotal.setText(total);
        String stok=tabeltrx.getValueAt(baris, 5).toString();
        txtstok.setText(stok);
        
        for(int y=0; y<6; y++)System.out.println(tabeltrx.getValueAt(baris, y).toString());
        
    }//GEN-LAST:event_tabeltrxMouseClicked

    private void ubahbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubahbtnActionPerformed
        String kd= barangcmb.getSelectedItem().toString();
        try {
            String sql="SELECT id_barang FROM tb_barang WHERE id_barang='"+kd+"'";
            Connection conn = konektor.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);
            if(res.next()){
                String query = "UPDATE tb_dtltransaksi SET id_barang='"+res.getString("id_barang")+"', jumlah_beli='"+txtjumlahbeli.getText()+"' WHERE id_barang='"+barangcmb.getSelectedItem()+"'";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.execute();
                JOptionPane.showMessageDialog(null, "MANTAP");
                tampildata();
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }reset();
    }//GEN-LAST:event_ubahbtnActionPerformed

    private void txtbayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbayarKeyPressed
        
    }//GEN-LAST:event_txtbayarKeyPressed

    private void txtbayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtbayarActionPerformed
        int bayart = Integer.parseInt(txtbayar.getText());
        int totalt = Integer.parseInt(txttotalharga.getText());
        if (bayart >= totalt){
            int kembaliant = bayart - totalt;
            txtkembalian.setText(Integer.toString(kembaliant));
        }else{
           JOptionPane.showMessageDialog(null, "Tidak bisa hutang gan!");
           txtbayar.setText(null);
           txtkembalian.setText(null);
        }
    }//GEN-LAST:event_txtbayarActionPerformed

    private void txtstokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtstokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtstokActionPerformed

    private static String getUser(){
        return new login().user;
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
              
                new transaksi().setVisible(true);
              //  new transaksi().txtuser.setText(getUser());
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox barangcmb;
    private javax.swing.JButton hapusbrgbtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton simpanbtn;
    private javax.swing.JTable tabeltrx;
    private javax.swing.JButton tambahbrgbtn;
    private javax.swing.JButton tutupbtn;
    private javax.swing.JTextField txtbayar;
    private javax.swing.JTextField txtharga;
    private javax.swing.JTextField txtjumlahbeli;
    private javax.swing.JTextField txtkembalian;
    private javax.swing.JTextField txtnamabarang;
    private javax.swing.JTextField txtstok;
    private javax.swing.JTextField txttotal;
    private javax.swing.JTextField txttotalharga;
    private javax.swing.JTextField txttransaksi;
    private javax.swing.JButton ubahbtn;
    // End of variables declaration//GEN-END:variables
}

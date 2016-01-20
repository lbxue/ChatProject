package com.alex.chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created width IntelliJ IDEA
 * User:lbxue
 * Date:2016/1/20 0020
 * Time:PM 3:30
 */
public class ChatClient extends JFrame {

    private JScrollPane jspanShow;      //��ʾ���Ĺ������
    private JPanel panEdit;             //�༭�����
    private JTextArea txtArea;          //��ʾ���ݵĶ����ı���
    private JTextField txtField;        //�ı������
    private JButton btnSend;            //���Ͱ�ť

    private Socket client;              //�ͻ���
    private BufferedReader br;          //�ַ�������
    private PrintWriter pw;             //�����

    public JScrollPane getJspanShow() {
        if(this.jspanShow == null){
            this.jspanShow = new JScrollPane(this.getTxtArea());
        }
        return jspanShow;
    }

    public JPanel getPanEdit() {
        if (this.panEdit == null) {
            this.panEdit = new JPanel();
            this.panEdit.add(this.getTxtField());
            this.panEdit.add(this.getBtnSend());
        }
        return panEdit;
    }

    public JTextArea getTxtArea() {
        if (this.txtArea == null) {
            this.txtArea = new JTextArea();
        }
        return txtArea;
    }

    public JTextField getTxtField() {
        if (this.txtField == null) {
            this.txtField = new JTextField(16);
        }
        return txtField;
    }

    public JButton getBtnSend() {
        if (this.btnSend == null) {
            this.btnSend = new JButton("����");
            this.btnSend.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String content = txtField.getText();
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
                    String time = sdf.format(date);
                    String sendContent = "�ͻ���"+time+"\n"+content;
                    pw.println(sendContent);
                    txtArea.append(sendContent+"\n");
                    txtField.setText("");
                }
            });
        }
        return btnSend;
    }

    //��ʼ��һ������
    private void init() {
        this.setSize(300, 400);
        this.setTitle("�ͻ���");
        this.setLocation(700, 200);
        this.addComponents();
        this.setVisible(true);
    }

    public ChatClient() {
        this.init();
        this.start();
    }

    /**
     * ��������
     */
    public void start() {
        try {
            client = new Socket("127.0.0.1",6789);
            this.br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.pw = new PrintWriter(client.getOutputStream(),true);
            new ListenerThread().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * �ڲ���ר�����������յ�����Ϣ
     */
    class ListenerThread extends Thread {
        @Override
        public void run(){
            String line = null;
            try {
                while ((line = br.readLine()) != null) {
                    txtArea.append(line+"\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ������������
     */
    private void addComponents() {
        Container contentPane = this.getContentPane();
        contentPane.add(this.getJspanShow());
        contentPane.add(this.getPanEdit(),BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        new ChatClient();
    }


}

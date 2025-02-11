package org.example;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

public class EmailSenderGUI extends JFrame {
    private JTextField destinatarioField;
    private JTextField asuntoField;
    private JTextArea mensajeArea;
    private JButton enviarButton;
    private JLabel statusLabel;

    public EmailSenderGUI() {
        // Configuración de la ventana
        setTitle("Enviar Correo Electrónico");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de entrada
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Etiquetas y campos
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        inputPanel.add(new JLabel("Destinatario:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        destinatarioField = new JTextField();
        inputPanel.add(destinatarioField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Asunto:"), gbc);

        gbc.gridx = 1;
        asuntoField = new JTextField();
        inputPanel.add(asuntoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Mensaje:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mensajeArea = new JTextArea(5, 20);
        mensajeArea.setLineWrap(true);
        mensajeArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(mensajeArea);
        inputPanel.add(scrollPane, gbc);

        // Botón de enviar
        enviarButton = new JButton("Enviar Correo");
        enviarButton.setBackground(new Color(0, 123, 255)); // Color azul
        enviarButton.setForeground(Color.WHITE);
        enviarButton.setFont(new Font("Arial", Font.BOLD, 14));
        enviarButton.setFocusPainted(false);
        enviarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarCorreo();
            }
        });

        // Label para mostrar el estado
        statusLabel = new JLabel("");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);

        // Añadir componentes a la ventana
        add(inputPanel, BorderLayout.CENTER);
        add(enviarButton, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);
    }

    private void enviarCorreo() {
        // Configuración de las propiedades del servidor SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Autenticación
        final String username = "smtp.rafa.pablo@gmail.com"; // Reemplaza con tu email
        final String password = "jmvl alwe rczh mvua"; // Reemplaza con tu contraseña

        // Crear la sesión con autenticación
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Obtener los datos de la interfaz
        String destinatario = destinatarioField.getText().trim();
        String asunto = asuntoField.getText().trim();
        String mensaje = mensajeArea.getText().trim();

        // Validaciones de entrada
        if (destinatario.isEmpty() || !destinatario.contains("@")) {
            statusLabel.setText("Por favor, introduce un correo electrónico válido.");
            return;
        }
        if (asunto.isEmpty()) {
            statusLabel.setText("Por favor, introduce un asunto.");
            return;
        }
        if (mensaje.isEmpty()) {
            statusLabel.setText("Por favor, introduce un mensaje.");
            return;
        }

        try {
            // Crear el mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(mensaje);

            // Enviar el mensaje
            Transport.send(message);
            statusLabel.setText("Correo enviado exitosamente");
            statusLabel.setForeground(new Color(0, 150, 0)); // Color verde

        } catch (MessagingException e) {
            statusLabel.setText("Error al enviar el correo");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmailSenderGUI gui = new EmailSenderGUI();
            gui.setVisible(true);
        });
    }
}

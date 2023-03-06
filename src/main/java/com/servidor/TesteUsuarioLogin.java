package com.servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class TesteUsuarioLogin {

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        // Altera a rota e adiciona os parâmetros para o GET
        HttpGet httpGet = new HttpGet("http://localhost:8080/usuarios?usuario=andre&senha=123");

        HttpResponse response = httpclient.execute(httpGet);

        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                String mensagem = builder.toString();
                System.out.println(mensagem);
            } else {
                System.out.println("Erro ao verificar usuário.");
            }
        } finally {
            response.getEntity().getContent().close();
        }
    }
}
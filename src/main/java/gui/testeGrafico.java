package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import model.Pedido;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class testeGrafico extends Application {

	@Override
	public void start(Stage stage) {
		stage.setTitle("Line Chart Example");
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Tipo de biscoito");
		yAxis.setLabel("Quantidade de ingredientes");

		final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

		lineChart.setTitle("Quantidade de ingredientes por tipo de biscoito");
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Ingredientes");

		// Adicionar dados ao gráfico
		series.getData().add(new XYChart.Data<>("Biscoito A", 20));
		series.getData().add(new XYChart.Data<>("Biscoito A", 15));
		series.getData().add(new XYChart.Data<>("Biscoito C", 25));
		series.getData().add(new XYChart.Data<>("Biscoito D", 10));

		Scene scene = new Scene(lineChart, 800, 600);
		lineChart.getData().add(series);

		stage.setScene(scene);
		stage.show();
	}
	
	public class LineChartPedidos extends Application {
	    @Override
	    public void start(Stage primaryStage) throws IOException {
	        // Define o eixo X como String
	        NumberAxis xAxis = new NumberAxis();
	        xAxis.setLabel("Tipo de Biscoito");

	        // Define o eixo Y como número
	        NumberAxis yAxis = new NumberAxis();
	        yAxis.setLabel("Quantidade de Ingredientes");

	        //LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
	       // lineChart.setTitle("Gráfico de Quantidade de Ingredientes por Tipo de Biscoito");
	        //lineChart.setCreateSymbols(false);

	        OkHttpClient client = new OkHttpClient();
	        Request request = new Request.Builder().url("http://localhost:8080/pedidos").build();
	        Response response = client.newCall(request).execute();

	        Pedido[] pedidos = new Gson().fromJson(response.body().string(), Pedido[].class);
	        List<XYChart.Data<String, Number>> dataList = new ArrayList<>();
	     //   for (Pedido pedido : pedidos) {
	      //      dataList.add(new XYChart.Data<>(pedido.getTipoBiscoito(), pedido.getQuantidadeIngredientes()));
	      //  }

	        XYChart.Series<String, Number> data = new XYChart.Series<>();
	        data.setName("Quantidade de Ingredientes");
	        data.getData().addAll(dataList);
	    //    lineChart.getData().add(data);

	   //     Scene scene = new Scene(lineChart, 800, 600);
	      //  primaryStage.setScene(scene);
	      //  primaryStage.show();
	    }
	}

	public static void main(String[] args) {
		launch(args);
	}
}
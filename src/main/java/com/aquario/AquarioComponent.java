package com.aquario;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

@Component
public class AquarioComponent {

	@Autowired
	private SlackService slack;

	private boolean isLigado;

	private GpioController gpio;
	private GpioPinDigitalOutput pin;

	public AquarioComponent() {
		slack.gravarSlack("atlantida", "ligado");

		gpio = GpioFactory.getInstance();
		pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.LOW);
		pin.setShutdownOptions(true, PinState.LOW);
	}

	// * "0 0 * * * *" = the top of every hour of every day.
	// * "*/10 * * * * *" = every ten seconds.
	// * "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
	// * "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
	// * "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
	// * "0 0 0 25 12 ?" = every Christmas Day at midnight

	@Scheduled(cron = "*/20 * * * * *")
	public void controleDeIluminacao() {
		if (LocalTime.now().getHour() >= 8 && LocalTime.now().getHour() <= 17 && !isLigado) {
			ligar();
		} else if (LocalTime.now().getHour() > 17 && LocalTime.now().getHour() < 8 && isLigado) {
			desligar();
		}

	}

	public void ligar() {
		pin.high();
		slack.gravarSlack("comando - Ligar", "aquario-comando");

		isLigado = true;
	}

	public void desligar() {
		slack.gravarSlack("comando - Desligar", "aquario-comando");
		isLigado = false;
	}

	@Scheduled(cron = "* */60 * * * *")
	public void status() {
		String status = this.isLigado ? "Ligado" : "Desligado";
		slack.gravarSlack("status - " + status + " :> lampanda acesa? " + pin.isHigh(), "aquario-status");
	}
}

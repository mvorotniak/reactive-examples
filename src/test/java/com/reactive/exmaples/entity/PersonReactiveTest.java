package com.reactive.exmaples.entity;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.CountDownLatch;

import com.reactive.exmaples.command.PersonCommand;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class PersonReactiveTest {

    private Person mariana;

    private Person sergiy;

    private Person irina;

    @BeforeEach
    void init() {
        this.mariana = new Person("Mariana", "V");
        this.sergiy = new Person("Sergiy", "D");
        this.irina = new Person("Irina", "P");
    }

    @Test
    void monoTests() {
        Mono<Person> personMono = Mono.just(mariana);

        log.info(personMono.block().getFullName());
    }

    @Test
    void monoTransform() {
        Mono<Person> personMono = Mono.just(sergiy);

        PersonCommand person = personMono
            .map(PersonCommand::new)
            .block();

        log.info(person.getFullName());
    }

    @Test
    void monoFilter() {
        Mono<Person> personMono = Mono.just(irina);

        Person person = personMono
            .filter(p -> p.getFirstName().equals("foo"))
            .block();

        assertThrows(NullPointerException.class, () -> person.getFullName());
    }

    @Test
    void fluxTest() {
        Flux<Person> personFlux = Flux.just(mariana, sergiy, irina);

        personFlux.subscribe(p -> log.info(p.getFullName()));
    }

    @Test
    void fluxTestFilter() {
        Flux<Person> personFlux = Flux.just(mariana, sergiy, irina);

        personFlux
            .filter(p -> p.getFirstName().contains("i"))
            .subscribe(p -> log.info(p.getFullName()));
    }

    @Test
    void fluxTestDelayNoOutput() {
        Flux<Person> personFlux = Flux.just(mariana, sergiy, irina);

        // Delay for 1 second. Because it's running in the background we didn't wait for the output
        personFlux.delayElements(Duration.of(1, ChronoUnit.SECONDS))
            .subscribe(p -> log.info(p.getFullName()));
    }

    @Test
    void fluxTestDelay() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Flux<Person> personFlux = Flux.just(mariana, sergiy, irina);

        personFlux.delayElements(Duration.of(1, ChronoUnit.SECONDS))
            .doOnComplete(countDownLatch::countDown)
            .subscribe(p -> log.info(p.getFullName()));

        // wait unitll countdown is complete
        countDownLatch.await();
    }

}

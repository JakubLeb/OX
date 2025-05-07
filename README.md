# OX – Gra Kółko i Krzyżyk w Javie

OX to prosta implementacja gry kółko i krzyżyk napisana w języku Java, z możliwością zapisywania wyników rozgrywek w bazie danych.

## Funkcje

* Gra dla dwóch graczy w trybie tekstowym.
* Zapisywanie historii rozgrywek w bazie danych.
* Obsługa podstawowej logiki gry i walidacji ruchów.

## Wymagania

* Java 8 lub nowsza.
* Maven (opcjonalnie, jeśli projekt korzysta z tego systemu budowania).
* Baza danych (np. MySQL, PostgreSQL) – konfiguracja połączenia znajduje się w pliku `db/config.properties`.

## Uruchomienie

1. Sklonuj repozytorium:

   ```bash
   git clone https://github.com/JakubLeb/OX.git
   cd OX
   ```

2. Skonfiguruj połączenie z bazą danych w pliku `db/config.properties`.

3. Skompiluj projekt i uruchom aplikację:

   ```bash
   javac -d bin src/*.java
   java -cp bin Main
   ```

## Struktura projektu

* `src/` – kod źródłowy aplikacji.
* `db/` – pliki konfiguracyjne bazy danych.
* `logs/` – katalog na logi aplikacji.
* `lib/` – zewnętrzne biblioteki (jeśli są używane).

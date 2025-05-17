# Dokumentation des Polizei-Managementsystems

### **Hinweis:** TestCommands sind in der TestCommands.md Datei

## 1. Domain-Driven Design

### 1.1 Analyse der Ubiquitous Language

Das Polizei-Managementsystem dreht sich um die Verwaltung von Aktivitäten einer Polizeistation, insbesondere Verhöre und Besprechungen. Bei der Analyse des Codes können wir folgende Schlüsselbegriffe identifizieren, die unsere Ubiquitous Language bilden:

**Primäre Domänenbegriffe:**

- **Officer (Polizist)**: Ein Polizeibeamter mit einem bestimmten Rang, der Verhöre und Besprechungen durchführt
- **Detainee (Gefangener)**: Eine Person, die wegen eines bestimmten Verbrechens in Gewahrsam gehalten wird
- **Room (Raum)**: Ein physischer Raum, der für verschiedene Aktivitäten gebucht werden kann (Verhör, Besprechung, Produktion)
- **Interrogation (Verhör)**: Eine formelle Befragungssitzung zwischen einem Polizisten und einem Gefangenen in einem Verhörraum
- **Meeting (Besprechung)**: Ein geplantes Treffen zwischen einem Polizisten und einem Informanten in einem Besprechungsraum
- **Rank (Rang)**: Hierarchische Position eines Polizisten in der Polizei (z.B. Officer, Sergeant, Inspector)

**Unterstützende Domänenbegriffe:**

- **Book/Release (Buchen/Freigeben)**: Aktionen, um einen Raum nicht verfügbar/verfügbar zu machen
- **Schedule (Planen)**: Planung einer Aktivität zu einem bestimmten Zeitpunkt
- **Crime (Verbrechen)**: Straftat, die von einem Gefangenen begangen wurde
- **Available (Verfügbar)**: Status eines Raums, der angibt, ob er gebucht werden kann
- **Promotion/Demotion (Beförderung/Degradierung)**: Änderung des Rangs eines Polizisten zu einem höheren/niedrigeren Niveau

Diese Begriffe bilden eine zusammenhängende Sprache, die Stakeholder und Entwickler nutzen können, um ohne Mehrdeutigkeit über das System zu kommunizieren. Die Ubiquitous Language spiegelt sich in der Codestruktur wider, wobei Klassen und Methoden entsprechend dieser Domänenkonzepte benannt sind.

### 1.2 Analyse und Begründung der eingesetzten DDD-Taktischen Muster

#### Value Objects

Value Objects in diesem System repräsentieren Konzepte, die durch ihre Attribute und nicht durch eine Identität definiert werden. Sie sind unveränderlich und haben keinen Lebenszyklus.

**Beispiel 1: `Rank`**

```java
public final class Rank implements Comparable<Rank> {
    private final String name;
    private final int level;
    
    // Constructor, equals, hashCode, etc.
}
```

**Begründung**: Die `Rank`-Klasse wird als Value Object implementiert, weil:
- Sie keine eigene Identität hat; zwei Ränge mit gleichem Namen und Level werden als gleich betrachtet
- Sie unveränderlich ist - einmal erstellt, können ihre Werte nicht geändert werden
- Sie Domänenregeln kapselt (wie den Vergleich von Rängen)
- Sie ausschließlich durch ihre Attribute (Name und Level) definiert wird

**Beispiel 2: `RoomType`**

```java
public enum RoomType {
    INTERROGATION,
    MEETING,
    PRODUCTION
}
```

**Begründung**: `RoomType` ist als Enum implementiert, was eine Form von Value Object ist, weil:
- Es eine festgelegte Menge möglicher Raumtypen in der Domäne darstellt
- Es keine Identität jenseits seines Wertes hat
- Es unveränderlich ist
- Es verwendet wird, um Räume nach ihrem Zweck zu klassifizieren

#### Entities

Entities sind Objekte mit einer eindeutigen Identität, die im gesamten System bestehen bleibt, auch wenn sich ihre Attribute ändern.

**Beispiel 1: `Officer`**

```java
public class Officer {
    private final UUID id;
    private final String name;
    private Rank rank;
    
    // Constructor, methods, etc.
}
```

**Begründung**: `Officer` wird als Entity implementiert, weil:
- Jeder Polizist eine eindeutige Identität (UUID) hat, die konstant bleibt
- Polizisten einen Lebenszyklus innerhalb des Systems haben
- Ihre Attribute (wie Rang) sich im Laufe der Zeit ändern können
- Die Domänenregeln für Beförderung und Degradierung innerhalb der Entity gekapselt sind

**Beispiel 2: `Room`**

```java
public class Room {
    private final UUID id;
    private final RoomType type;
    private boolean available = true;
    
    // Constructor, methods, etc.
}
```

**Begründung**: `Room` wird als Entity implementiert, weil:
- Jeder Raum eine eindeutige Identität hat
- Sein Zustand sich ändern kann (er kann gebucht oder freigegeben werden)
- Er Geschäftsregeln durchsetzt (z.B. kann ein Raum nicht gebucht werden, wenn er bereits gebucht ist)

#### Aggregates

Aggregates gruppieren Entities und Value Objects, die als eine einzige Einheit für Datenänderungen behandelt werden sollten.

**Beispiel 1: `Interrogation` Aggregate**

```java
public class Interrogation {
    private final UUID id;
    private final Officer officer;
    private final Detainee detainee;
    private final Room room;
    private final LocalDateTime scheduledAt;
    
    // Constructor, methods, etc.
}
```

**Begründung**: `Interrogation` wird als Aggregate implementiert, weil:
- Es verwandte Entities (Officer, Detainee, Room) gruppiert, die als Einheit sinnvoll sind
- Es Invarianten über diese Entities hinweg durchsetzt (z.B. muss der Raum vom Typ INTERROGATION sein)
- Es eine klare Aggregate Root hat (das Verhör selbst)
- Änderungen an jedem Teil eines Verhörs über die Aggregate Root erfolgen sollten

**Beispiel 2: `Meeting` Aggregate**

```java
public class Meeting {
    private final UUID id;
    private final Officer officer;
    private final String informantName;
    private final Room room;
    private final LocalDateTime scheduledAt;
    
    // Constructor, methods, etc.
}
```

**Begründung**: `Meeting` wird als Aggregate implementiert, weil:
- Es verwandte Elemente (Officer, Room, Informanteninformationen) zu einer logischen Einheit gruppiert
- Es Geschäftsregeln durchsetzt (z.B. muss der Raum vom Typ MEETING sein)
- Es eine klare Grenze hat, die seine Komponenten kapselt

#### Repositories

Repositories stellen Methoden zum Zugriff auf und zur Persistierung von Aggregates bereit und verbergen die Details des zugrunde liegenden Datenspeichers.

**Beispiel 1: `OfficerRepository`**

```java
public interface OfficerRepository {
    void save(Officer officer);
    Optional<Officer> findById(UUID id);
    List<Officer> findAll();
    void deleteById(UUID id);
}
```

**Begründung**: `OfficerRepository` folgt dem Repository-Pattern, weil:
- Es eine sammlungsähnliche Schnittstelle für den Zugriff auf Officer-Entities bietet
- Es den Persistenzmechanismus abstrahiert
- Es sich auf Operationen konzentriert, die mit einem bestimmten Aggregate (Officer) zusammenhängen
- Es domänenfokussierte Abfragen anstelle generischer Datenzugriffsmethoden unterstützt

**Beispiel 2: `InterrogationRepository`**

```java
public interface InterrogationRepository {
    void save(Interrogation interrogation);
    Optional<Interrogation> findById(UUID id);
    List<Interrogation> findAll();
    List<Interrogation> findByOfficerId(UUID officerId);
    // Weitere Methoden...
}
```

**Begründung**: `InterrogationRepository` ist ein Repository, weil:
- Es domänenspezifische Abfragemethoden bietet (z.B. `findByOfficerId`)
- Es den Persistenzmechanismus verbirgt
- Es das Interrogation-Aggregate als Einheit behandelt

#### Domain Services

Domain Services kapseln Geschäftslogik, die nicht natürlicherweise in eine einzelne Entity oder ein Value Object passt.

**Beispiel 1: `OfficerService`**

```java
public class OfficerService {
    private final OfficerRepository officerRepository;
    
    // Constructor, Methoden wie registerOfficer, promoteOfficer, etc.
}
```

**Begründung**: `OfficerService` wird als Domain Service implementiert, weil:
- Er Operationen implementiert, die mehrere Entities betreffen (Finden von Polizisten mit einem Mindestrang)
- Er Geschäftslogik enthält, die nicht natürlicherweise zu einer einzelnen Entity gehört
- Er Operationen zwischen Entities und Repositories koordiniert

**Beispiel 2: `InterrogationService`**

```java
public class InterrogationService {
    private static final Rank MINIMUM_RANK_FOR_INTERROGATION = new Rank("Sergeant", 3);
    private final RoomRepository roomRepository;
    private final OfficerRepository officerRepository;
    private final InterrogationRepository interrogationRepository;
    
    // Constructor, Methoden wie scheduleInterrogation, etc.
}
```

**Begründung**: `InterrogationService` ist ein Domain Service, weil:
- Er Domänenregeln durchsetzt, die mehrere Aggregates betreffen (z.B. Ranganforderungen für Polizisten)
- Er die Erstellung von Verhören koordiniert, was mehrere Entities involviert
- Er komplexe Geschäftslogik enthält, die nicht zu einer bestimmten Entity gehört

## 2. Clean Architecture

### 2.1 Schematische Architektur und Begründung

Das Polizei-Managementsystem implementiert einen Clean Architecture-Ansatz, wobei der Code in verschiedene Schichten mit einer klaren Abhängigkeitsregel organisiert ist: Abhängigkeiten zeigen nur nach innen.

Die Architektur besteht aus folgenden Schichten:

1. **Domain-Schicht (3-ASE2025-domain)**
    - Enthält Geschäftsentitäten, Value Objects, Repository-Interfaces und Domain Services
    - Repräsentiert die Kern-Geschäftsregeln und -Konzepte
    - Hat keine Abhängigkeiten von anderen Schichten (hängt nur von der Abstraction Code-Schicht ab)

2. **Anwendungsschicht (2-ASE2025-application)**
    - Enthält Use Cases, die die Domain-Schicht orchestrieren
    - Implementiert anwendungsspezifische Geschäftsregeln
    - Hängt nur von der Domain-Schicht ab

3. **Adapter-Schicht (1-ASE2025-adapters)**
    - Dient als Brücke zwischen dem Anwendungskern und externen Komponenten
    - Umfasst Schnittstellen, die Daten zwischen dem für Entities und Use Cases günstigsten Format und dem für externe Anwendungen günstigsten Format konvertieren
    - Hängt von der Anwendungsschicht ab

4. **Plugins-Schicht (0-ASE2025-plugins)**
    - Enthält Implementierungen von in inneren Schichten definierten Schnittstellen
    - Umfasst Infrastrukturbelange wie Repositories, UI, Datenbanken usw.
    - Hängt von der Adapter-Schicht ab

5. **Abstraction Code-Schicht (4-ASE2025-abstractioncode)**
    - Enthält gemeinsame Abstraktionen und Hilfsfunktionen
    - Wird von allen anderen Schichten verwendet

### 2.2 Begründung für die Schichtenarchitektur

Der Clean Architecture-Ansatz wurde aus mehreren Gründen gewählt:

1. **Trennung der Zuständigkeiten**:
    - Jede Schicht hat eine spezifische Verantwortung, was den Code leichter verständlich und wartbar macht
    - Domänenregeln sind von Anwendungsanwendungsfällen getrennt, die wiederum von Zustellungsmechanismen getrennt sind

2. **Unabhängigkeit von Frameworks**:
    - Die Kern-Geschäftslogik (Domain- und Anwendungsschichten) ist unabhängig von externen Frameworks
    - Ein Wechsel von einem Framework zu einem anderen würde nur die Plugins-Schicht betreffen

3. **Testbarkeit**:
    - Kern-Geschäftslogik kann ohne UI, Datenbank, Webserver oder andere externe Elemente getestet werden
    - Abhängigkeiten können für Testzwecke leicht gemockt werden

4. **Unabhängigkeit der Benutzeroberfläche**:
    - Die UI kann sich ändern, ohne die Geschäftsregeln zu beeinflussen
    - Das System könnte über eine CLI, eine Web-Schnittstelle oder einen anderen Mechanismus zugänglich sein, ohne die Kernlogik zu ändern

5. **Unabhängigkeit der Datenbank**:
    - Die Geschäftsregeln sind nicht an eine bestimmte Datenbank gebunden
    - Das System könnte mit minimalen Auswirkungen auf die Kernlogik die Datenbank wechseln

6. **Unabhängigkeit von externen Systemen**:
    - Geschäftsregeln wissen nichts über die Außenwelt
    - Die Interaktion mit externen Systemen erfolgt über in den inneren Schichten definierte Schnittstellen

### 2.3 Abhängigkeitsmanagement

Die Architektur erzwingt die Dependency Rule: Quellcode-Abhängigkeiten zeigen nur nach innen. Innere Schichten definieren Schnittstellen, und äußere Schichten implementieren sie. Diese Umkehrung der Kontrolle zeigt sich in:

- Repository-Interfaces, die in der Domain-Schicht definiert und in der Plugins-Schicht implementiert werden
- Use Cases in der Anwendungsschicht, die von in der Domain-Schicht definierten Repositories abhängen
- Die CLI in der Plugins-Schicht, die von in der Anwendungsschicht definierten Use Cases abhängt

## 3. Programming Principles (Programmierprinzipien)

### 3.1 Single Responsibility Principle (SRP)

Das Single Responsibility Principle besagt, dass eine Klasse nur einen Grund haben sollte, sich zu ändern. Dieses Prinzip wird im gesamten Code angewendet.

**Beispiel**: `RoomManagementService` Klasse

```java
public class RoomManagementService {
    private final RoomRepository roomRepository;
    
    // Methoden für raumbezogene Operationen
}
```

**Begründung**: Diese Klasse hat eine einzige Verantwortung: die Verwaltung raumbezogener Operationen. Sie behandelt keine Polizisten, Gefangene oder Terminplanungslogik. Wenn sich die Anforderungen an die Raumverwaltung ändern, muss nur diese Klasse modifiziert werden.

### 3.2 Open/Closed Principle (OCP)

Das Open/Closed Principle besagt, dass Softwareentitäten für Erweiterungen offen, aber für Modifikationen geschlossen sein sollten.

**Beispiel**: Repository-Interfaces und -Implementierungen

```java
// Domain-Schicht
public interface RoomRepository {
    void save(Room room);
    Optional<Room> findById(UUID id);
    // Andere Methoden...
}

// Plugins-Schicht
public class JsonRoomRepository implements RoomRepository {
    // Implementation...
}
```

**Begründung**: Die Repository-Interfaces sind für Modifikationen geschlossen (wir ändern die Schnittstelle nicht), aber das System ist durch verschiedene Implementierungen für Erweiterungen offen. Neue Speichermechanismen können hinzugefügt werden, ohne die Kern-Geschäftslogik zu modifizieren.

### 3.3 Dependency Inversion Principle (DIP)

Das Dependency Inversion Principle besagt, dass Module höherer Ebene nicht von Modulen niedrigerer Ebene abhängen sollten; beide sollten von Abstraktionen abhängen.

**Beispiel**: Use Cases, die von Repository-Interfaces abhängen

```java
public class GetAllRoomsUseCase {
    private final RoomManagementService roomManagementService;
    
    // Konstruktor und execute-Methode...
}
```

**Begründung**: Der Use Case (Modul höherer Ebene) hängt vom RoomManagementService ab, der wiederum vom RoomRepository-Interface (Abstraktion) abhängt, nicht von konkreten Implementierungen. Diese Umkehrung ermöglicht einfacheres Testen und flexible Implementierungen.

### 3.4 Interface Segregation Principle (ISP)

Das Interface Segregation Principle besagt, dass Clients nicht gezwungen werden sollten, von Schnittstellen abzuhängen, die sie nicht verwenden.

**Beispiel**: Repository-Interfaces

```java
public interface InterrogationRepository {
    void save(Interrogation interrogation);
    Optional<Interrogation> findById(UUID id);
    List<Interrogation> findByOfficerId(UUID officerId);
    List<Interrogation> findByDetaineeId(UUID detaineeId);
    // Andere spezifische Methoden...
}
```

**Begründung**: Jedes Repository-Interface enthält nur die Methoden, die für seinen Aggregattyp relevant sind. Zum Beispiel enthält das `InterrogationRepository` spezifische Methoden für Verhöre wie `findByOfficerId` und `findByDetaineeId`, während das `DetaineeRepository` andere Methoden enthält. Dies verhindert, dass Clients von Methoden abhängen, die sie nicht benötigen.

### 3.5 DRY (Don't Repeat Yourself)

Das DRY-Prinzip zielt darauf ab, die Wiederholung von Softwaremustern zu reduzieren, indem sie durch Abstraktionen oder Datennormalisierung ersetzt werden.

**Beispiel**: `AbstractCommand` Klasse

```java
public abstract class AbstractCommand implements Command {
    private final String name;
    private final String description;
    private final String usage;
    
    // Konstruktor und gemeinsame Methoden...
}
```

**Begründung**: Die `AbstractCommand`-Klasse vermeidet die Wiederholung von gemeinsamem Command-Verhalten über alle Command-Implementierungen hinweg. Sie kapselt gemeinsames Verhalten wie Name, Beschreibung und Verwaltung der Nutzung sowie die Anzeige von Nachrichten auf der Konsole.

## 5. Refactoring

### 5.1 Code Smells

#### Long Method in `InterrogationService.scheduleInterrogation`

In der `InterrogationService`-Klasse ist die Methode `scheduleInterrogation` sehr lang und hat mehrere Verantwortlichkeiten:

##### Warum das ein Problem ist:

- Die Methode übernimmt mehrere Aufgaben: Suche nach Offizieren, Validierung von Rängen, Überprüfung der Verfügbarkeit, Suche nach Räumen und Buchung.
- Sie ist schwieriger zu testen und zu warten, da sie mehrere Verantwortlichkeiten hat.
- Die Methode hat eine hohe kognitive Komplexität, was sie auf den ersten Blick schwer verständlich macht.

> **Verstoß gegen das Single Responsibility Principle:**  
> Die Methode tut zu viele Dinge gleichzeitig. Sie sollte überarbeitet werden, indem kleinere Methoden extrahiert werden, die jeweils eine bestimmte Verantwortlichkeit übernehmen.

---

#### Primitive Obsession bei Rang-Vergleichen

Im `Rank`-Value Object und in verschiedenen Teilen des Codes sehen wir primitive Typen, die für Domänenkonzepte verwendet werden:

##### Warum das ein Problem ist:

- Das Rangsystem wird durch primitive Ganzzahlen repräsentiert, was die Hierarchie der Polizeiränge nicht explizit kommuniziert.
- Hart codierte Rang-Levels sind über den Codebase verstreut.
- Dies macht das System anfällig – wenn Rang-Levels geändert oder komplexe Regeln hinzugefügt werden müssen, müssten viele Stellen aktualisiert werden.

> **Besserer Ansatz:**  
> Verwendung eines `Enum` mit vordefinierten Rang-Levels und effektive Kapselung der Beförderungs-/Degradierungslogik, idealerweise mit einem speziellen Domain-Service für das Rollenmanagement.

---

#### God Class / Große Klasse in `CliRunner`

Die `CliRunner`-Klasse ist ein klassisches Beispiel für das "God Class"-Antimuster:

##### Warum das ein Problem ist:

- Die Klasse hat zu viele Verantwortlichkeiten – sie behandelt CLI-Setup, Repositories, Services, Use Cases und Befehlsregistrierung.
- Sie verstößt gegen das Single Responsibility Principle, da sie zu viele Dinge tut.
- Die Klasse hat eine große Anzahl von Abhängigkeiten, was sie schwer testbar und wartbar macht.
- Änderungen an einem Subsystem erfordern wahrscheinlich Änderungen in dieser Klasse.

> **Verbesserungsvorschlag:**  
> Aufteilung in mehrere fokussierte Klassen – z. B. separate Klassen für Repositories, Services und Befehlsregistrierung – macht den Code wartbarer und testbarer.


### 5.2 Refactorings:

#### Problem:
Der Mindestrang für ein Verhör ist als *Magic Number* direkt im Code verankert (`new Rank("Sergeant", 3)`), was die Wartbarkeit beeinträchtigt und das Risiko von Inkonsistenzen erhöht.

#### Lösung:
Extrahieren des Magic Numbers als **symbolische Konstante** und zentrale Definition des Mindestrangs.

#### Begründung:

- **Bessere Lesbarkeit**: Die Konstante hat einen aussagekräftigen Namen, der ihren Zweck kommuniziert.
- **Verbesserte Wartbarkeit**: Wenn sich der Mindestrang ändert, muss er nur an einer Stelle aktualisiert werden.
- **Reduziertes Risiko von Inkonsistenzen**: Durch die zentrale Definition des Mindestrangs wird verhindert, dass der Rang an verschiedenen Stellen im Code unterschiedlich definiert wird.
- **Self-documenting Code**: Der Code dokumentiert sich selbst besser, die Bedeutung des Rangs wird durch den Konstantennamen erklärt.
- **DRY-Prinzip**: Die Definition des Mindestrangs wird nicht an mehreren Stellen wiederholt (*Don't Repeat Yourself*).

#### Problem:
Viele Parameterübergaben in der InterrogationService-Klasse.

#### Lösung:
Einführung eines Value Object `InterrogationRequest` mit allen benötigten Informationen.

#### Begründung:

### Vorteile der Parameter-Objekt-Einführung

- **Bessere Lesbarkeit**: Die Methode nimmt aktuell drei verschiedene Parameter an (`officerId`, `detainee`, `scheduledTime`). Durch die Zusammenfassung in ein Objekt wird sofort klar, dass diese Parameter zusammengehören.
- **Erhöhte Wartbarkeit**:  Wenn in Zukunft weitere Parameter hinzukommen müssen (z.B. *Priorität der Befragung* oder *Notizen*), muss die Methodensignatur nicht geändert werden – man fügt einfach neue Attribute zum Parameter-Objekt hinzu.
- **Validierung an einer zentralen Stelle**: Im Parameter-Objekt können Validierungen durchgeführt werden (z.B. keine `null`-Werte), sodass die Validierungslogik zentral an einem Ort stattfindet.
- **Reduzierte Parameteranzahl**: Methoden mit vielen Parametern sind schwerer zu lesen und zu verwenden. Das Zusammenfassen reduziert die Komplexität.
- **Domain-Ausrichtung**: Das neue Objekt `InterrogationRequest` repräsentiert ein Konzept der Domäne und macht den Code daher ausdrucksstärker.






## 6. Entwurfsmuster

### 6.1 Repository-Muster

Das Repository-Muster bietet eine sammlungsartige Schnittstelle für den Zugriff auf Domänenobjekte.

**Implementierung**:

```java
public interface RoomRepository {
    void save(Room room);
    Optional<Room> findById(UUID id);
    List<Room> findAll();
    List<Room> findByType(RoomType type);
    List<Room> findAvailable();
    List<Room> findAvailableByType(RoomType type);
    void deleteById(UUID id);
}
```

**Begründung**:
- **Entkopplung**: Die Domain-Schicht ist von Datenzugriffstechnologien entkoppelt
- **Testbarkeit**: Services und Use Cases können mit Mock-Repositories getestet werden
- **Vereinfachter Client-Code**: Clients arbeiten mit einer sammlungsartigen Schnittstelle anstelle komplexer Abfragen
- **Domänenfokus**: Abfragen werden in der Domänensprache und nicht in der Persistenzsprache ausgedrückt
- **Optimierter Datenzugriff**: Repository-Implementierungen können Abfragen basierend auf dem zugrunde liegenden Speichermechanismus optimieren

### 6.2 Command-Muster

Das Command-Muster kapselt eine Anfrage als Objekt und ermöglicht so die Parametrisierung von Clients mit verschiedenen Anfragen.

**Implementierung**:

```java
public interface Command {
    boolean execute(String[] args);
    String getName();
    String getDescription();
    String getUsage();
}

public abstract class AbstractCommand implements Command {
    // Implementierung...
}

public class CreateRoomCommand extends AbstractCommand {
    private final CreateRoomUseCase createRoomUseCase;
    
    // Konstruktor und execute-Methode...
}
```

**Begründung**:
- **Entkoppelte Ausführung**: Commands kapseln die Logik für die Ausführung spezifischer Aktionen
- **Erweiterbarkeit**: Neue Commands können hinzugefügt werden, ohne bestehenden Code zu ändern
- **Command-Registry**: Commands können bei einem zentralen Handler registriert werden
- **Parameterhandling**: Jedes Command behandelt seine eigenen Parameter
- **Dokumentation**: Commands enthalten Selbstdokumentation (Beschreibung und Verwendung)

### 6.3 Factory-Muster

Das Factory-Muster bietet eine Schnittstelle zum Erstellen von Objekten, ohne die konkreten Klassen anzugeben.

**Implementierung**:

```java
public class JsonRepositoryFactory {
    private JsonRoomRepository roomRepository;
    private JsonOfficerRepository officerRepository;
    private JsonDetaineeRepository detaineeRepository;
    private JsonInterrogationRepository interrogationRepository;
    private JsonMeetingRepository meetingRepository;
    
    // Factory-Methoden...
}
```

**Begründung**:
- **Kapselung der Erstellungslogik**: Komplexe Objekterstellungslogik wird in einer dedizierten Klasse gekapselt
- **Abhängigkeitsmanagement**: Die Factory verwaltet Abhängigkeiten zwischen Repository-Implementierungen
- **Einzelne Verantwortung**: Objekterstellung ist von Objektnutzung getrennt
- **Konsistenz**: Stellt sicher, dass alle Repository-Objekte mit korrekter Initialisierung erstellt werden

### 6.4 Strategy-Muster

Das Strategy-Muster definiert eine Familie von Algorithmen, kapselt jeden einzelnen und macht sie austauschbar.

**Implementierung**:
Die Repository-Abstraktionen und ihre Implementierungen stellen eine Form des Strategy-Musters dar:

```java
// Strategy-Interface
public interface RoomRepository {
    // Methoden...
}

// Konkrete Strategien
public class InMemoryRoomRepository implements RoomRepository {
    // Implementierung...
}

public class JsonRoomRepository implements RoomRepository {
    // Implementierung...
}
```

**Begründung**:
- **Algorithmuskapselung**: Jede Repository-Implementierung kapselt ihre eigene Datenzugriffsstrategie
- **Austauschbarkeit**: Implementierungen können ausgetauscht werden, ohne Clients zu beeinflussen
- **Beseitigung bedingter Logik**: Keine Notwendigkeit für bedingte Logik zur Auswahl zwischen verschiedenen Datenzugriffsmethoden
- **Erweiterung**: Neue Strategien können hinzugefügt werden, ohne bestehenden Code zu ändern
- **Laufzeitflexibilität**: Die spezifische Strategie kann zur Laufzeit ausgewählt werden

### 6.5 Decorator-Muster

Das Decorator-Muster fügt einem Objekt dynamisch zusätzliche Verantwortlichkeiten hinzu.

**Implementierung**:
Obwohl nicht explizit implementiert, unterstützt die Architektur Decorator-ähnliches Verhalten durch Repository-Wrapper:

```java
// Hypothetische Implementierung
public class LoggingRoomRepository implements RoomRepository {
    private final RoomRepository delegate;
    
    // Konstruktor und dekorierte Methoden...
}
```

**Begründung**:
- **Funktionalitätserweiterung**: Repositories könnten umhüllt werden, um Logging, Caching oder andere Querschnittsbelange hinzuzufügen
- **Einzelne Verantwortung**: Jeder Decorator konzentriert sich auf eine einzelne zusätzliche Verantwortung
- **Komposition über Vererbung**: Verwendet Objektkomposition anstelle von Vererbung zur Erweiterung des Verhaltens
- **Flexibilität**: Decorators können auf verschiedene Weise kombiniert werden, um unterschiedliche Funktionalitäten zu erreichen

## Fazit

Das Polizei-Managementsystem demonstriert eine gut strukturierte Anwendung, die Domain-Driven Design-Prinzipien, Clean Architecture und solide Programmierpraktiken befolgt. Die klare Trennung der Zuständigkeiten, der Domänenfokus und die Verwendung etablierter Muster und Prinzipien schaffen ein wartbares, testbares und erweiterbares System.

Die Implementierung modelliert erfolgreich die Polizeidomäne und erfasst die Beziehungen zwischen Polizisten, Gefangenen, Räumen, Verhören und Besprechungen. Die Architektur stellt sicher, dass Geschäftsregeln zentralisiert und vor externen Belangen geschützt sind, während die Verwendung von Schnittstellen und Dependency Inversion Flexibilität bei Implementierungsdetails bietet.

Durch die Anwendung dieser Prinzipien und Muster erreicht das System das Ziel, eine nachhaltige Codebasis zu schaffen, die sich mit sich ändernden Anforderungen weiterentwickeln kann, während sie ihre Kernintegrität bewahrt.
# Guide de Démonstration du Projet

Ce guide vous accompagne pas à pas pour compiler et exécuter les différents composants du projet (Collector et Augmentation).

---

## 🛠️ Partie 1 : Le Collector

Le collector est responsable de la récupération et de la structuration des données initiales.

### Démo 1 — Compiler le collector

Compilez le module Java du collector :

```bash
mvn -f .\java\collector\pom.xml clean install
```

### Démo 2 — Lancer le collector

Exécutez le collector pour commencer la récupération des données :

```bash
mvn --% -f .\java\collector\pom.xml exec:java -Dexec.mainClass=VRAIE_MAIN_CLASS
```

### Démo 3 — Explorer les paires collectées

Une fois l'exécution terminée, vérifiez la structure des données dans le dossier `dataset` :

#### Nombre total de paires :

```powershell
(Get-ChildItem -Directory .\dataset\pairs).Count
```

#### Contenu d'une paire (exemple) :

```powershell
$pair = Get-ChildItem -Directory .\dataset\pairs | Select-Object -First 1
dir $pair.FullName
Get-Content ($pair.FullName + "\manifest.json")
```

---

## 🧪 Partie 2 : L'Augmentation

L'augmentation traite les paires collectées pour générer un dataset plus riche.

### Démo 4 — Compiler l'augmentation

Compilez le module Java dédié à l'augmentation :

```bash
mvn -f .\java\augmentation\pom.xml clean install
```

### Démo 5 — Lancer l'augmentation

Lancez le processus de transformation des données :

```bash
mvn --% -f .\java\augmentation\pom.xml exec:java -Dexec.mainClass=VRAIE_MAIN_CLASS
```

### Démo 6 — Vérifier les données augmentées

Vérifiez que les nouvelles données ont bien été générées dans le dossier `augmented_pairs` :

```powershell
(Get-ChildItem -Directory .\dataset\augmented_pairs).Count
```

---

## ⚙️ Prérequis

- **Maven** installé et configuré dans votre variable d'environnement `PATH`
- **Java** (version appropriée pour votre projet)

---

## 📝 Notes importantes

- Remplacez `VRAIE_MAIN_CLASS` par le véritable nom de la classe principale
- Assurez-vous d'être dans le répertoire racine du projet avant d'exécuter les commandes
- Chaque commande doit être copiée intégralement dans votre terminal PowerShell ou CMD
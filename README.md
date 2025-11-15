# caribean Good Vybzz - Site Vitrine Association

![Logo caribean Good Vybzz](src/main/resources/static/images/logo.jpg)

## 📋 Description

Site vitrine professionnel pour l'association **caribean Good Vybzz**, développé avec Spring Boot 3.x et Java 17+. Le site permet la gestion complète des membres, des messages de contact et d'une galerie de photos et vidéos.

---

## 🎯 Fonctionnalités

### Pages Publiques
- **Page d'accueil** : Présentation de l'association et de sa mission
- **Page d'inscription** : Formulaire pour devenir membre
- **Page de contact** : Formulaire de contact pour envoyer des messages
- **Galerie média** : Affichage des photos et vidéos de l'association

### Interface d'Administration
- **Dashboard** : Vue d'ensemble avec statistiques
- **Gestion des membres** : Validation, modification de statut, suppression
- **Gestion des messages** : Lecture, marquage comme lu, suppression
- **Gestion des médias** : Upload de photos, ajout de vidéos (YouTube, Vimeo), publication/dépublication

---

## 🛠️ Technologies Utilisées

- **Java** : 17+
- **Spring Boot** : 3.2.0
- **Spring Security** : Authentification et autorisation
- **Spring Data JPA** : Persistance des données
- **Thymeleaf** : Moteur de templates HTML
- **H2 Database** : Base de données pour le développement
- **PostgreSQL** : Base de données pour la production
- **Lombok** : Réduction du code boilerplate
- **Maven** : Gestion des dépendances

---

## 📁 Structure du Projet

```
caribean-good-vybzz/
├── src/
│   ├── main/
│   │   ├── java/com/caribean/goodvybzz/
│   │   │   ├── config/              # Configuration (Security, Data Init)
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/          # Contrôleurs MVC
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── MembershipController.java
│   │   │   │   ├── ContactController.java
│   │   │   │   ├── MediaController.java
│   │   │   │   └── AdminController.java
│   │   │   ├── model/               # Entités JPA
│   │   │   │   ├── Member.java
│   │   │   │   ├── Contact.java
│   │   │   │   ├── Media.java
│   │   │   │   └── AdminUser.java
│   │   │   ├── repository/          # Repositories JPA
│   │   │   │   ├── MemberRepository.java
│   │   │   │   ├── ContactRepository.java
│   │   │   │   ├── MediaRepository.java
│   │   │   │   └── AdminUserRepository.java
│   │   │   ├── service/             # Services métier
│   │   │   │   ├── MemberService.java
│   │   │   │   ├── ContactService.java
│   │   │   │   ├── MediaService.java
│   │   │   │   └── AdminUserService.java
│   │   │   └── caribeanGoodVybzzApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css    # Styles CSS
│   │       │   ├── js/
│   │       │   │   └── script.js    # Scripts JavaScript
│   │       │   ├── images/
│   │       │   │   └── logo.jpg     # Logo de l'association
│   │       │   └── media/           # Dossier pour les uploads
│   │       ├── templates/
│   │       │   ├── index.html       # Page d'accueil
│   │       │   ├── inscription.html # Page d'inscription
│   │       │   ├── contact.html     # Page de contact
│   │       │   ├── media.html       # Galerie de médias
│   │       │   └── admin/           # Pages d'administration
│   │       │       ├── login.html
│   │       │       ├── dashboard.html
│   │       │       ├── members.html
│   │       │       ├── contacts.html
│   │       │       └── manage-media.html
│   │       └── application.properties
│   └── test/                        # Tests unitaires
├── pom.xml                          # Configuration Maven
└── README.md                        # Ce fichier
```

---

## 🚀 Installation et Démarrage

### Prérequis
- Java 17 ou supérieur
- Maven 3.6+
- (Optionnel) PostgreSQL pour la production

### Étapes d'Installation

1. **Cloner le projet** (ou extraire l'archive)
```bash
cd caribean-good-vybzz
```

2. **Compiler le projet**
```bash
mvn clean install
```

3. **Lancer l'application**
```bash
mvn spring-boot:run
```

4. **Accéder à l'application**
- Site public : http://localhost:8080
- Interface admin : http://localhost:8080/admin/login
- Console H2 (dev) : http://localhost:8080/h2-console

### Identifiants par Défaut (Admin)
```
Nom d'utilisateur : admin
Mot de passe : admin123
```

⚠️ **IMPORTANT** : Changez ces identifiants en production !

---

## ⚙️ Configuration

### Base de Données H2 (Développement)
Par défaut, l'application utilise H2. La configuration se trouve dans `application.properties`.

### Passage à PostgreSQL (Production)

1. **Installer PostgreSQL**

2. **Créer la base de données**
```sql
CREATE DATABASE caribeangoodvybzz;
```

3. **Modifier `application.properties`**
```properties
# Commenter la configuration H2
#spring.datasource.url=jdbc:h2:file:./data/caribeangoodvybzz
#spring.h2.console.enabled=true

# Décommenter et configurer PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/caribeangoodvybzz
spring.datasource.username=votre_utilisateur
spring.datasource.password=votre_mot_de_passe
spring.datasource.driverClassName=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

---

## 📖 Guide d'Utilisation

### Pour les Visiteurs

1. **S'inscrire** : Cliquez sur "Inscription" et remplissez le formulaire
2. **Contacter** : Utilisez le formulaire de contact pour envoyer un message
3. **Galerie** : Consultez les photos et vidéos de l'association

### Pour les Administrateurs

1. **Se connecter** : Allez sur `/admin/login` et entrez vos identifiants
2. **Tableau de bord** : Vue d'ensemble des statistiques
3. **Gérer les membres** :
   - Voir tous les membres inscrits
   - Changer le statut (Actif, En attente, Inactif)
   - Supprimer des membres
4. **Gérer les messages** :
   - Lire les messages reçus
   - Marquer comme lu
   - Supprimer des messages
5. **Gérer les médias** :
   - Uploader des photos
   - Ajouter des vidéos (URL YouTube/Vimeo)
   - Publier/Dépublier des médias
   - Supprimer des médias

---

## 🔒 Sécurité

### Mesures Implémentées
- **Authentification** : Spring Security avec cryptage BCrypt
- **Protection CSRF** : Activée par défaut (désactivée pour H2 console en dev)
- **Autorisation** : Pages admin protégées par authentification
- **Validation** : Validation côté serveur et client

### Recommandations pour la Production
1. Changer le mot de passe admin par défaut
2. Désactiver la console H2
3. Utiliser HTTPS
4. Configurer un mot de passe fort pour PostgreSQL
5. Limiter les tailles d'upload de fichiers
6. Mettre en place des sauvegardes régulières

---

## 🎨 Personnalisation

### Modifier les Couleurs
Éditez le fichier `src/main/resources/static/css/style.css` :
```css
:root {
    --primary-color: #FF6B35;      /* Orange vif */
    --secondary-color: #FFB100;    /* Jaune doré */
    --accent-color: #4ECDC4;       /* Turquoise */
    /* ... */
}
```

### Changer le Logo
Remplacez `src/main/resources/static/images/logo.jpg` par votre logo.

### Modifier les Textes
Les textes des pages se trouvent dans les fichiers HTML du dossier `templates/`.

---

## 📝 Documentation du Code

### Architecture Modulaire
Le projet suit une architecture en couches pour faciliter la maintenance et la répartition des tâches :

- **model** : Entités de données (Member, Contact, Media, AdminUser)
- **repository** : Accès aux données (interfaces JPA)
- **service** : Logique métier
- **controller** : Contrôleurs MVC
- **config** : Configuration de l'application

### JavaDoc
Toutes les classes sont documentées avec JavaDoc. Pour générer la documentation :
```bash
mvn javadoc:javadoc
```
La documentation sera dans `target/site/apidocs/`.

---

## 🧪 Tests

### Lancer les Tests
```bash
mvn test
```

### Ajouter des Tests
Les tests se trouvent dans `src/test/java/com/caribean/goodvybzz/`.

---

## 📦 Déploiement

### Créer un JAR Exécutable
```bash
mvn clean package
```
Le JAR sera dans `target/goodvybzz-1.0.0.jar`.

### Lancer le JAR
```bash
java -jar target/goodvybzz-1.0.0.jar
```

### Déployer sur un Serveur
1. Copier le JAR sur le serveur
2. Configurer PostgreSQL
3. Créer un service systemd (Linux) :
```bash
sudo nano /etc/systemd/system/caribeangoodvybzz.service
```
```ini
[Unit]
Description=caribean Good Vybzz
After=network.target

[Service]
User=www-data
WorkingDirectory=/opt/caribeangoodvybzz
ExecStart=/usr/bin/java -jar goodvybzz-1.0.0.jar
SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
```

---

## 🐛 Dépannage

### Problème : Le site ne démarre pas
- Vérifiez que Java 17+ est installé : `java -version`
- Vérifiez que le port 8080 est libre
- Consultez les logs dans la console

### Problème : Erreur de connexion à la base de données
- Vérifiez la configuration dans `application.properties`
- Assurez-vous que PostgreSQL est démarré (en production)

### Problème : Impossible de se connecter en admin
- Utilisez les identifiants par défaut : admin / admin123
- Si changés, utilisez vos identifiants personnalisés

---

## 👥 Contribution

### Répartition des Tâches
L'architecture modulaire permet de répartir le développement :
- **Équipe Frontend** : Templates HTML/CSS/JS
- **Équipe Backend** : Services et contrôleurs
- **Équipe Base de données** : Modèles et repositories
- **Équipe Sécurité** : Configuration security

### Standards de Code
- Java : Suivre les conventions Java
- Documentation : JavaDoc obligatoire pour toutes les classes
- Tests : Écrire des tests pour les nouvelles fonctionnalités

---

## 📄 Licence

Ce projet est la propriété de l'association caribean Good Vybzz.

---

## 📞 Contact

Association caribean Good Vybzz
- Site web : http://localhost:8080 (en développement)
- Email : contact@caribeangoodvybzz.org

---

## 🙏 Remerciements

Développé avec ❤️ pour promouvoir la culture caribéenne 🌴🥁

---

## 📚 Ressources Utiles

- [Documentation Spring Boot](https://spring.io/projects/spring-boot)
- [Documentation Thymeleaf](https://www.thymeleaf.org/documentation.html)
- [Documentation Spring Security](https://spring.io/projects/spring-security)
- [Documentation JPA](https://spring.io/guides/gs/accessing-data-jpa/)

---

**Version** : 1.0.0  
**Date** : Janvier 2025  
**Auteur** : caribean Good Vybzz Development Team

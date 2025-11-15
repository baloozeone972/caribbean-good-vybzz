#!/bin/bash

# Script de Vérification Pré-Déploiement
# Caribbean Good Vybzz
# Ce script vérifie que tout est prêt avant le déploiement

echo "================================================"
echo "  Caribbean Good Vybzz - Vérification Pré-Déploiement"
echo "================================================"
echo ""

# Compteur d'erreurs
ERRORS=0

# Couleurs pour l'affichage
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Fonction pour afficher OK
print_ok() {
    echo -e "${GREEN}✓${NC} $1"
}

# Fonction pour afficher erreur
print_error() {
    echo -e "${RED}✗${NC} $1"
    ((ERRORS++))
}

# Fonction pour afficher avertissement
print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

# 1. Vérifier Java
echo "1. Vérification de Java..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -ge 17 ]; then
        print_ok "Java $JAVA_VERSION installé"
    else
        print_error "Java 17+ requis (version actuelle : $JAVA_VERSION)"
    fi
else
    print_error "Java n'est pas installé"
fi
echo ""

# 2. Vérifier Maven
echo "2. Vérification de Maven..."
if command -v mvn &> /dev/null; then
    MVN_VERSION=$(mvn -version | head -n 1 | cut -d' ' -f3)
    print_ok "Maven $MVN_VERSION installé"
else
    print_error "Maven n'est pas installé"
fi
echo ""

# 3. Vérifier Git
echo "3. Vérification de Git..."
if command -v git &> /dev/null; then
    GIT_VERSION=$(git --version | cut -d' ' -f3)
    print_ok "Git $GIT_VERSION installé"
    
    # Vérifier si on est dans un repo Git
    if git rev-parse --git-dir > /dev/null 2>&1; then
        print_ok "Repository Git détecté"
        
        # Vérifier les modifications non commitées
        if [[ -n $(git status -s) ]]; then
            print_warning "Modifications non commitées détectées"
            git status -s
        else
            print_ok "Pas de modifications non commitées"
        fi
    else
        print_error "Pas de repository Git trouvé"
    fi
else
    print_error "Git n'est pas installé"
fi
echo ""

# 4. Vérifier la structure du projet
echo "4. Vérification de la structure du projet..."
FILES_TO_CHECK=(
    "pom.xml"
    "src/main/java/com/caribean/goodvybzz/CaribbeanGoodVybzzApplication.java"
    "src/main/resources/application.properties"
    "README.md"
)

for file in "${FILES_TO_CHECK[@]}"; do
    if [ -f "$file" ]; then
        print_ok "Fichier trouvé : $file"
    else
        print_error "Fichier manquant : $file"
    fi
done
echo ""

# 5. Vérifier les fichiers de déploiement
echo "5. Vérification des fichiers de déploiement..."
DEPLOY_FILES=(
    "Dockerfile"
    "docker-compose.yml"
    "render.yaml"
    ".env.example"
    "application-prod.properties"
)

for file in "${DEPLOY_FILES[@]}"; do
    if [ -f "$file" ]; then
        print_ok "Fichier de déploiement trouvé : $file"
    else
        print_warning "Fichier de déploiement manquant : $file (optionnel)"
    fi
done
echo ""

# 6. Tester la compilation
echo "6. Test de compilation Maven..."
if mvn clean compile -DskipTests > /dev/null 2>&1; then
    print_ok "Compilation réussie"
else
    print_error "Échec de la compilation"
    echo "   Exécutez 'mvn clean compile' pour voir les détails"
fi
echo ""

# 7. Vérifier les dépendances
echo "7. Vérification des dépendances Maven..."
if mvn dependency:resolve > /dev/null 2>&1; then
    print_ok "Toutes les dépendances sont résolues"
else
    print_error "Problème avec les dépendances Maven"
fi
echo ""

# 8. Vérifier le fichier .env
echo "8. Vérification de la configuration d'environnement..."
if [ -f ".env" ]; then
    print_ok "Fichier .env trouvé"
    
    # Vérifier les variables critiques
    if grep -q "DB_PASSWORD=changeme" .env 2>/dev/null; then
        print_warning "Le mot de passe de la base de données utilise la valeur par défaut"
    fi
    
    if grep -q "ADMIN_PASSWORD=admin123" .env 2>/dev/null; then
        print_warning "Le mot de passe admin utilise la valeur par défaut"
    fi
else
    print_warning "Fichier .env non trouvé (créez-le depuis .env.example pour Docker)"
fi
echo ""

# 9. Vérifier Docker (si installé)
echo "9. Vérification de Docker (optionnel)..."
if command -v docker &> /dev/null; then
    DOCKER_VERSION=$(docker --version | cut -d' ' -f3 | tr -d ',')
    print_ok "Docker $DOCKER_VERSION installé"
    
    if command -v docker-compose &> /dev/null; then
        COMPOSE_VERSION=$(docker-compose --version | cut -d' ' -f4 | tr -d ',')
        print_ok "Docker Compose $COMPOSE_VERSION installé"
    else
        print_warning "Docker Compose n'est pas installé (optionnel)"
    fi
else
    print_warning "Docker n'est pas installé (optionnel pour déploiement local)"
fi
echo ""

# 10. Recommandations de sécurité
echo "10. Vérifications de sécurité..."
if [ -f "application-prod.properties" ]; then
    if grep -q "admin123" application-prod.properties 2>/dev/null; then
        print_warning "Mot de passe admin par défaut dans application-prod.properties"
        echo "   Changez-le avant le déploiement en production !"
    else
        print_ok "Pas de mot de passe par défaut détecté"
    fi
fi
echo ""

# Résumé
echo "================================================"
echo "  RÉSUMÉ"
echo "================================================"

if [ $ERRORS -eq 0 ]; then
    echo -e "${GREEN}✓ Toutes les vérifications essentielles sont passées !${NC}"
    echo ""
    echo "Prochaines étapes :"
    echo "  1. Choisir une plateforme de déploiement (Render, Railway, Docker)"
    echo "  2. Consulter GUIDE_DEPLOIEMENT.md pour les instructions détaillées"
    echo "  3. Configurer les variables d'environnement de production"
    echo "  4. Déployer l'application"
    exit 0
else
    echo -e "${RED}✗ $ERRORS erreur(s) détectée(s)${NC}"
    echo ""
    echo "Veuillez corriger les erreurs avant de déployer."
    exit 1
fi

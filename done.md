### Tâche : Implémentation de la vérification du premier login et du changement de mot de passe

**Heure de début :** samedi 19 juillet 2025, 23:35
**Heure de fin :** samedi 19 juillet 2025, 23:36

**Description de la tâche demandée :**
Modifier la méthode de connexion (`login`) pour vérifier si les champs `createdAt` et `updatedAt` de l'utilisateur sont identiques. Si c'est le cas, empêcher la connexion et demander à l'utilisateur de changer son mot de passe. Implémenter une nouvelle méthode `changePassword` qui prend l'email, l'ancien mot de passe et le nouveau mot de passe, met à jour le mot de passe de l'utilisateur et retourne un nouveau token.

**Description du travail effectué et résultats :**
1.  **Création d'une exception personnalisée :**
    -   Création de `PasswordChangeRequiredException.java` dans `org.beni.gestionboisson.auth.exceptions`.
2.  **Création d'un DTO pour le changement de mot de passe :**
    -   Création de `ChangePasswordRequestDTO.java` dans `org.beni.gestionboisson.auth.dto` avec les champs `email`, `oldPassword` et `newPassword`.
3.  **Modification de l'interface `AuthService` :**
    -   Mise à jour de la signature de la méthode `login` pour qu'elle puisse lancer `PasswordChangeRequiredException`.
    -   Ajout de la méthode `changePassword(ChangePasswordRequestDTO request)`.
4.  **Modification de l'implémentation `AuthServiceImpl` :**
    -   Dans la méthode `login` :
        -   Changement de la recherche de l'utilisateur de `findByNomUtilisateur` à `findByEmail`.
        -   Ajout de la logique de vérification `utilisateur.getCreatedAt().equals(utilisateur.getUpdatedAt())`. Si vrai, lance `PasswordChangeRequiredException`.
    -   Implémentation de la méthode `changePassword` :
        -   Récupération de l'utilisateur par email.
        -   Vérification de l'ancien mot de passe avec `BCrypt.checkpw`.
        -   Hachage et mise à jour du nouveau mot de passe.
        -   Mise à jour du champ `updatedAt`.
        -   Sauvegarde de l'utilisateur.
        -   Génération et retour d'un nouveau token JWT.
5.  **Modification de `AuthController` :**
    -   Mise à jour de l'endpoint `/auth/login` pour intercepter `PasswordChangeRequiredException` et retourner une réponse HTTP 403 (Forbidden) avec un message spécifique.
    -   Ajout d'un nouvel endpoint `POST /auth/change-password` qui accepte `ChangePasswordRequestDTO` et appelle la méthode `changePassword` du service `AuthService`.

**Résultats :**
Le système d'authentification est renforcé avec une politique de changement de mot de passe obligatoire après le premier login. Une fonctionnalité de changement de mot de passe sécurisée a été ajoutée, permettant aux utilisateurs de mettre à jour leurs identifiants et de recevoir un nouveau token de session.

### Tâche : Implémentation de la logique de Refresh Token

**Heure de début :** samedi 19 juillet 2025, 23:36
**Heure de fin :** samedi 19 juillet 2025, 23:37

**Description de la tâche demandée :**
Ajouter la logique de refresh token pour permettre aux utilisateurs de renouveler leur jeton d'accès sans se reconnecter, et expliquer son fonctionnement et son utilisation.

**Description du travail effectué et résultats :**
1.  **Mise à jour de l'entité `Utilisateur` :**
    -   Ajout du champ `refreshToken` pour stocker le jeton de rafraîchissement de l'utilisateur.
2.  **Mise à jour de `JwtUtil` :**
    -   Séparation des secrets et des durées d'expiration pour les jetons d'accès (`accessToken`) et les jetons de rafraîchissement (`refreshToken`).
    -   Renommage de `generateToken` en `generateAccessToken`.
    -   Ajout de la méthode `generateRefreshToken` pour créer des jetons de rafraîchissement.
    -   Renommage de `validateToken` en `validateAccessToken`.
    -   Ajout de la méthode `validateRefreshToken` pour valider les jetons de rafraîchissement.
3.  **Mise à jour de `AuthResponseDTO` :**
    -   Modification des champs pour inclure `accessToken` et `refreshToken`.
4.  **Mise à jour de l'interface `AuthService` :**
    -   Ajout de la méthode `refreshAccessToken(String refreshToken)`.
5.  **Mise à jour de l'implémentation `AuthServiceImpl` :**
    -   Dans la méthode `login` :
        -   Génération et sauvegarde du `refreshToken` dans l'entité `Utilisateur` après une connexion réussie.
        -   Retour de l'`accessToken` et du `refreshToken` dans `AuthResponseDTO`.
    -   Dans la méthode `changePassword` :
        -   Génération et sauvegarde d'un nouveau `refreshToken` après un changement de mot de passe réussi.
    -   Implémentation de la méthode `refreshAccessToken` :
        -   Validation du `refreshToken` fourni.
        -   Récupération de l'utilisateur associé au `refreshToken`.
        -   Vérification que le `refreshToken` fourni correspond à celui stocké pour l'utilisateur.
        -   Génération d'un nouvel `accessToken` et d'un nouveau `refreshToken`.
        -   Mise à jour du `refreshToken` de l'utilisateur dans la base de données.
        -   Retour des nouveaux jetons.
6.  **Mise à jour de `AuthController` :**
    -   Ajout d'un nouveau DTO `RefreshTokenRequestDTO` pour encapsuler le jeton de rafraîchissement dans la requête.
    -   Ajout d'un nouvel endpoint `POST /auth/refresh` qui accepte `RefreshTokenRequestDTO` et appelle la méthode `refreshAccessToken` du service `AuthService`.

**Fonctionnement et Utilisation des Refresh Tokens :**

**Pourquoi les Refresh Tokens ?**
Les jetons d'accès (Access Tokens) sont généralement de courte durée (par exemple, 1 heure) pour minimiser le risque en cas de vol. Cependant, cela signifierait que les utilisateurs devraient se reconnecter fréquemment. Les jetons de rafraîchissement (Refresh Tokens) résolvent ce problème en étant de longue durée (par exemple, 7 jours ou plus) et en étant utilisés pour obtenir de nouveaux jetons d'accès.

**Comment ça marche ?**
1.  **Connexion Initiale :** Lorsqu'un utilisateur se connecte avec succès (via `/auth/login`), le serveur lui délivre deux jetons :
    *   Un **Access Token** (jeton d'accès) : court-vécu, utilisé pour accéder aux ressources protégées de l'API.
    *   Un **Refresh Token** (jeton de rafraîchissement) : long-vécu, utilisé uniquement pour obtenir un nouvel Access Token.
2.  **Stockage des Tokens :** Le client (application front-end, mobile, etc.) stocke l'Access Token (généralement en mémoire ou dans un stockage sécurisé comme le `localStorage` ou `sessionStorage` pour les applications web, ou le `keychain` pour les applications mobiles) et le Refresh Token (dans un stockage plus persistant et sécurisé, comme les `HttpOnly cookies` pour les applications web, ou le `keychain` pour les applications mobiles).
3.  **Accès aux Ressources :** Le client utilise l'Access Token pour faire des requêtes aux endpoints protégés de l'API. Le `JwtFilter` intercepte ces requêtes, valide l'Access Token et autorise l'accès si le jeton est valide et non expiré.
4.  **Expiration de l'Access Token :** Lorsque l'Access Token expire (ou est sur le point d'expirer), le client ne peut plus accéder aux ressources protégées.
5.  **Renouvellement de l'Access Token :** Au lieu de demander à l'utilisateur de se reconnecter, le client envoie le Refresh Token à l'endpoint `/auth/refresh`. Le serveur valide ce Refresh Token. S'il est valide et non révoqué, le serveur génère un **nouveau Access Token** et un **nouveau Refresh Token** (c'est une bonne pratique de faire tourner les Refresh Tokens pour une sécurité accrue).
6.  **Révocation :** Les Refresh Tokens peuvent être révoqués par le serveur (par exemple, si l'utilisateur se déconnecte de tous les appareils, ou si une activité suspecte est détectée). Cela invalide tous les Access Tokens générés par ce Refresh Token.

**Où l'utiliser ?**
*   **Côté Client (Front-end/Mobile) :**
    *   Après une connexion réussie, stockez l'Access Token et le Refresh Token.
    *   Avant chaque requête à une ressource protégée, vérifiez la validité de l'Access Token. Si expiré, utilisez le Refresh Token pour obtenir un nouveau couple de jetons via l'endpoint `/auth/refresh`.
    *   En cas de déconnexion de l'utilisateur, envoyez une requête au serveur pour révoquer le Refresh Token.
*   **Côté Serveur (Votre API) :**
    *   L'endpoint `/auth/login` émet les deux jetons et stocke le Refresh Token dans la base de données (`Utilisateur.refreshToken`).
    *   L'endpoint `/auth/refresh` valide le Refresh Token, génère de nouveaux jetons, et met à jour le Refresh Token stocké dans la base de données.
    *   Le `JwtFilter` valide uniquement l'Access Token pour l'accès aux ressources protégées.

**Avantages :**
*   **Sécurité Améliorée :** Les Access Tokens de courte durée réduisent la fenêtre d'opportunité pour les attaquants en cas de compromission.
*   **Meilleure Expérience Utilisateur :** Les utilisateurs restent connectés plus longtemps sans avoir à se reconnecter manuellement.
*   **Contrôle :** La révocation des Refresh Tokens permet de déconnecter un utilisateur à distance ou d'invalider des sessions compromises.

**Considérations :**
*   **Stockage Sécurisé :** Le Refresh Token doit être stocké de manière très sécurisée côté client et serveur, car sa compromission peut permettre à un attaquant de générer de nouveaux Access Tokens.
*   **Rotation des Refresh Tokens :** La rotation des Refresh Tokens (émettre un nouveau Refresh Token à chaque fois qu'un Access Token est rafraîchi) est une bonne pratique de sécurité.
*   **Gestion des Expirations :** Le client doit gérer intelligemment l'expiration des Access Tokens et le processus de rafraîchissement.
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { AuthService } from '../../services/auth.service';
import { LoginComponent } from './login.component';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

// Forcer TypeScript à reconnaître les types Jest
/// <reference types="jest" />

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;

  // Avant chaque test, on configure le module de test et on initialise les dépendances
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent], // On déclare le composant à tester
      providers: [SessionService, AuthService], // On fournit les services nécessaires
      imports: [
        RouterTestingModule, // Module pour tester la navigation
        BrowserAnimationsModule, // Module pour les animations
        HttpClientTestingModule, // Module pour simuler les requêtes HTTP
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule // Module pour les formulaires réactifs
      ]
    })
      .compileComponents(); // On compile les composants

    // On crée une instance du composant et on récupère les services injectés
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService); // On récupère une instance du service AuthService
    sessionService = TestBed.inject(SessionService); // On récupère une instance du service SessionService
    fixture.detectChanges(); // On déclenche la détection de changements pour initialiser le composant
  });

  // Test 1 : Vérifier que le composant est bien créé
  it('should create', () => {
    expect(component).toBeTruthy(); // On s'assure que le composant est bien instancié
  });

  // Test 2 : Vérifier que la méthode submit() appelle authService.login et gère la réponse correctement
  it('should call authService.login and navigate on successful login', () => {
    // Données de test
    const loginRequest = { email: 'test@example.com', password: 'password' };
    const sessionInformation: SessionInformation = {
      token: 'token',
      username: 'username',
      type: 'type',
      id: 1,
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true
    };

    // On simule une réponse réussie de authService.login
    jest.spyOn(authService, 'login').mockReturnValue(of(sessionInformation));

    // On espionne les méthodes navigate du Router et logIn du SessionService
    const navigateSpy = jest.spyOn(component['router'], 'navigate');
    const logInSpy = jest.spyOn(sessionService, 'logIn');

    // On remplit le formulaire avec les données de test
    component.form.setValue(loginRequest);

    // On appelle la méthode submit() du composant
    component.submit();

    // Assertions :
    // 1. Vérifier que authService.login a été appelé avec les bonnes données
    expect(authService.login).toHaveBeenCalledWith(loginRequest);

    // 2. Vérifier que sessionService.logIn a été appelé avec les informations de session
    expect(logInSpy).toHaveBeenCalledWith(sessionInformation);

    // 3. Vérifier que la navigation vers '/sessions' a été déclenchée
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  // Test 3 : Vérifier que la variable onError est définie à true en cas d'erreur de connexion
  it('should set onError to true on login error', () => {
    // Données de test
    const loginRequest = { email: 'test@example.com', password: 'password' };

    // On simule une erreur lors de l'appel à authService.login
    jest.spyOn(authService, 'login').mockReturnValue(throwError(() => new Error('error')));

    // On remplit le formulaire avec les données de test
    component.form.setValue(loginRequest);

    // On appelle la méthode submit() du composant
    component.submit();

    // Assertion : Vérifier que onError est bien défini à true
    expect(component.onError).toBe(true);
  });

  // Test 4 : Vérifier que le formulaire est invalide si un champ obligatoire est manquant
  it('should mark form as invalid if required fields are missing', () => {
    // Cas 1 : Les deux champs sont vides
    component.form.setValue({ email: '', password: '' });
    expect(component.form.invalid).toBe(true); // Le formulaire doit être invalide

    // Cas 2 : Le champ email est rempli, mais le mot de passe est vide
    component.form.setValue({ email: 'test@example.com', password: '' });
    expect(component.form.invalid).toBe(true); // Le formulaire doit être invalide

    // Cas 3 : Le champ email est vide, mais le mot de passe est rempli
    component.form.setValue({ email: '', password: 'password' });
    expect(component.form.invalid).toBe(true); // Le formulaire doit être invalide

    // Cas 4 : Les deux champs sont remplis
    component.form.setValue({ email: 'test@example.com', password: 'password' });
    expect(component.form.valid).toBe(true); // Le formulaire doit être valide
  });
});
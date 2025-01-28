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
import { AuthService } from '../../services/auth.service';
import { RegisterComponent } from './register.component';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;

  // Avant chaque test, on configure le module de test et on initialise les dépendances
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent], // On déclare le composant à tester
      providers: [AuthService], // On fournit les services nécessaires
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
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService); // On récupère une instance du service AuthService
    fixture.detectChanges(); // On déclenche la détection de changements pour initialiser le composant
  });

  // Test 1 : Vérifier que le composant est bien créé
  it('should create', () => {
    expect(component).toBeTruthy(); // On s'assure que le composant est bien instancié
  });

  // Test 2 : Vérifier que la méthode submit() appelle authService.register et gère la réponse correctement
  it('should call authService.register and navigate to login page on successful registration', () => {
    // Données de test
    const registerRequest: RegisterRequest = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password'
    };

    // On simule une réponse réussie de authService.register
    jest.spyOn(authService, 'register').mockReturnValue(of(undefined)); // Utiliser of(undefined) pour retourner Observable<void>

    // On espionne la méthode navigate du Router
    const navigateSpy = jest.spyOn(component['router'], 'navigate');

    // On remplit le formulaire avec les données de test
    component.form.setValue(registerRequest);

    // On appelle la méthode submit() du composant
    component.submit();

    // Assertions :
    // 1. Vérifier que authService.register a été appelé avec les bonnes données
    expect(authService.register).toHaveBeenCalledWith(registerRequest);

    // 2. Vérifier que la navigation vers '/login' a été déclenchée
    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
  });

  // Test 3 : Vérifier que la variable onError est définie à true en cas d'erreur de création de compte
  it('should set onError to true on registration error', () => {
    // Données de test
    const registerRequest: RegisterRequest = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password'
    };

    // On simule une erreur lors de l'appel à authService.register
    jest.spyOn(authService, 'register').mockReturnValue(throwError(() => new Error('error')));

    // On remplit le formulaire avec les données de test
    component.form.setValue(registerRequest);

    // On appelle la méthode submit() du composant
    component.submit();

    // Assertion : Vérifier que onError est bien défini à true
    expect(component.onError).toBe(true);
  });

  // Test 4 : Vérifier que le formulaire est invalide si un champ obligatoire est manquant
  it('should mark form as invalid if required fields are missing', () => {
    // Cas 1 : Tous les champs sont vides
    component.form.setValue({ email: '', firstName: '', lastName: '', password: '' });
    expect(component.form.invalid).toBe(true); // Le formulaire doit être invalide

    // Cas 2 : Le champ email est rempli, mais les autres champs sont vides
    component.form.setValue({ email: 'test@example.com', firstName: '', lastName: '', password: '' });
    expect(component.form.invalid).toBe(true); // Le formulaire doit être invalide

    // Cas 3 : Le champ email est vide, mais les autres champs sont remplis
    component.form.setValue({ email: '', firstName: 'John', lastName: 'Doe', password: 'password' });
    expect(component.form.invalid).toBe(true); // Le formulaire doit être invalide

    // Cas 4 : Tous les champs sont remplis
    component.form.setValue({ email: 'test@example.com', firstName: 'John', lastName: 'Doe', password: 'password' });
    expect(component.form.valid).toBe(true); // Le formulaire doit être valide
  });
});


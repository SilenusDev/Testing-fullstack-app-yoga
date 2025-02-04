import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';
import { MeComponent } from './me.component';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockSessionService: any;
  let mockUserService: any;
  let mockRouter: any;
  let mockSnackBar: any;

  // Définition des données mockées pour les services
  const mockSessionInformation = {
    admin: true,
    id: 1,
  };

  const mockUser = {
    id: 1,
    username: 'testuser',
    firstName: 'John',
    lastName: 'Doe',
  };

  beforeEach(async () => {
    // Création des mocks pour les services et les modules
    mockSessionService = {
      sessionInformation: mockSessionInformation,
      logOut: jest.fn(),
    };

    mockUserService = {
      getById: jest.fn().mockReturnValue(of(mockUser)),
      delete: jest.fn().mockReturnValue(of(null)),
    };

    mockRouter = {
      navigate: jest.fn(),
    };

    mockSnackBar = {
      open: jest.fn(),
    };

    // Configuration du module de test avec les mocks et les modules nécessaires
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockSnackBar },
      ],
    }).compileComponents();

    // Création du composant et détection des changements
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    // Vérifie que le composant est créé correctement
    expect(component).toBeTruthy();
  });

  it('should fetch user information on init', () => {
    // Vérifie que la méthode getById est appelée avec le bon ID
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    // Vérifie que l'utilisateur est défini correctement
    expect(component.user).toEqual(mockUser);
  });

  it('should navigate back on back method', () => {
    // Création d'un mock pour window.history.back
    const backSpy = jest.spyOn(window.history, 'back');

    // Appel de la méthode back()
    component.back();

    // Vérifie que la méthode history.back est appelée
    expect(backSpy).toHaveBeenCalled();
  });

  it('should delete user and log out on delete method', () => {
    // Appel de la méthode delete()
    component.delete();

    // Vérifie que la méthode delete est appelée avec le bon ID
    expect(mockUserService.delete).toHaveBeenCalledWith('1');

    // Vérifie que la méthode logOut est appelée
    expect(mockSessionService.logOut).toHaveBeenCalled();

    // Vérifie que la méthode navigate est appelée avec la bonne route
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);

    // Vérifie que la méthode open de MatSnackBar est appelée avec le bon message
    expect(mockSnackBar.open).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
  });
});

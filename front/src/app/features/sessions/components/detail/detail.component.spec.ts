import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { DetailComponent } from './detail.component';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionService: SessionService;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;

  // Mock du service SessionService pour simuler les données et comportements nécessaires
  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  // Mock du service SessionApiService pour simuler les appels API
  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of({
      id: 1,
      name: 'Test Session',
      description: 'Test Description',
      date: new Date(),
      createdAt: new Date(),
      updatedAt: new Date(),
      users: [1],
      teacher_id: 1
    })),
    delete: jest.fn().mockReturnValue(of(null)),
    participate: jest.fn().mockReturnValue(of(null)),
    unParticipate: jest.fn().mockReturnValue(of(null))
  };

  // Mock du service TeacherService pour simuler les appels API
  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of({
      firstName: 'John',
      lastName: 'Doe'
    }))
  };

  beforeEach(async () => {
    // Configuration du module de test Angular
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,  // Module pour simuler le routeur Angular
        HttpClientModule,     // Module pour simuler les requêtes HTTP
        MatSnackBarModule,    // Module pour simuler les notifications MatSnackBar
        MatCardModule,        // Module pour mat-card
        MatIconModule,        // Module pour mat-icon
        MatButtonModule,      // Module pour mat-button
        ReactiveFormsModule   // Module pour les formulaires réactifs
      ],
      declarations: [DetailComponent],  // Déclaration du composant à tester
      providers: [
        { provide: SessionService, useValue: mockSessionService },  // Fournir le mock du service SessionService
        { provide: SessionApiService, useValue: mockSessionApiService },  // Fournir le mock du service SessionApiService
        { provide: TeacherService, useValue: mockTeacherService }  // Fournir le mock du service TeacherService
      ],
    }).compileComponents();  // Compilation des composants déclarés

    // Injection des services mockés pour les utiliser dans les tests
    sessionService = TestBed.inject(SessionService);
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);

    // Création d'une instance du composant et de son fixture (environnement de test)
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    // Détection des changements pour initialiser le composant
    fixture.detectChanges();
  });

  it('should create', () => {
    // Test pour vérifier que le composant est créé avec succès
    // expect(component).toBeTruthy() vérifie que l'instance du composant n'est pas null ou undefined
    expect(component).toBeTruthy();
  });

  it('should fetch session details on init', () => {
    // Test pour vérifier que les détails de la session sont récupérés à l'initialisation
    expect(component.session).toBeDefined();
    expect(component.teacher).toBeDefined();
    expect(sessionApiService.detail).toHaveBeenCalledWith(component.sessionId);
    expect(teacherService.detail).toHaveBeenCalledWith('1');
  });

  it('should delete session', () => {
    // Test pour vérifier que la session est supprimée correctement
    component.delete();
    expect(sessionApiService.delete).toHaveBeenCalledWith(component.sessionId);
  });

  it('should participate in session', () => {
    // Test pour vérifier que l'utilisateur peut participer à la session
    component.participate();
    expect(sessionApiService.participate).toHaveBeenCalledWith(component.sessionId, component.userId);
  });

  it('should unparticipate in session', () => {
    // Test pour vérifier que l'utilisateur peut ne plus participer à la session
    component.unParticipate();
    expect(sessionApiService.unParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
  });

  it('should navigate back', () => {
    // Test pour vérifier que la navigation en arrière fonctionne
    const spy = jest.spyOn(window.history, 'back');
    component.back();
    expect(spy).toHaveBeenCalled();
  });
});

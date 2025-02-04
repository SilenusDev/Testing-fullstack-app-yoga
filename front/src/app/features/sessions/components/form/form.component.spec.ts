import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { FormComponent } from './form.component';
import { RouterTestingModule } from '@angular/router/testing';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let mockSessionApiService: any;
  let mockTeacherService: any;
  let mockRouter: any;
  let mockSnackBar: any;

  // Mock de SessionService pour simuler les informations de session
  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
  };

  beforeEach(async () => {
    // Mock de SessionApiService pour simuler les appels API
    mockSessionApiService = {
      create: jest.fn().mockReturnValue(of({})),
      update: jest.fn().mockReturnValue(of({})),
      detail: jest.fn().mockReturnValue(
        of({
          id: 1,
          name: 'Yoga Session',
          date: new Date('2023-12-01'),
          teacher_id: 1,
          description: 'Relaxation exercises',
        })
      ),
    };

    // Mock de TeacherService pour simuler la récupération des professeurs
    mockTeacherService = {
      all: jest.fn().mockReturnValue(
        of([
          { id: 1, firstName: 'John', lastName: 'Doe' },
          { id: 2, firstName: 'Jane', lastName: 'Smith' },
        ])
      ),
    };

    // Mock de Router pour simuler la navigation
    mockRouter = {
      navigate: jest.fn(),
      url: '/sessions/create',
    };

    // Mock de MatSnackBar pour simuler les notifications
    mockSnackBar = {
      open: jest.fn(),
    };

    // Configuration du module de test avec les mocks et les modules nécessaires
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockSnackBar },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: { get: jest.fn(() => '1') } } },
        },
      ],
      declarations: [FormComponent],
    }).compileComponents();

    // Création du composant et détection des changements
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // Test pour vérifier que le composant est créé
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test pour vérifier l'initialisation du formulaire en mode création
  it('should initialize form in create mode', () => {
    expect(component.onUpdate).toBeFalsy();
    expect(component.sessionForm?.value).toEqual({
      name: '',
      date: '',
      teacher_id: '',
      description: '',
    });
  });

  // Test pour vérifier l'initialisation du formulaire en mode mise à jour
  it('should initialize form in update mode', () => {
    mockRouter.url = '/sessions/update/1';
    component.ngOnInit();
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
    expect(component.onUpdate).toBeTruthy();
    expect(component.sessionForm?.value).toEqual({
      name: 'Yoga Session',
      date: '2023-12-01',
      teacher_id: 1,
      description: 'Relaxation exercises',
    });
  });

  // Test pour vérifier l'appel de la méthode de création lors de la soumission du formulaire en mode création
  it('should call create method on submit in create mode', () => {
    component.sessionForm?.setValue({
      name: 'Yoga Session',
      date: '2023-12-01',
      teacher_id: 1,
      description: 'Relaxation exercises',
    });

    component.submit();
    expect(mockSessionApiService.create).toHaveBeenCalledWith({
      name: 'Yoga Session',
      date: '2023-12-01',
      teacher_id: 1,
      description: 'Relaxation exercises',
    });
    expect(mockSnackBar.open).toHaveBeenCalledWith(
      'Session created !',
      'Close',
      { duration: 3000 }
    );
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  // Test pour vérifier l'appel de la méthode de mise à jour lors de la soumission du formulaire en mode mise à jour
  it('should call update method on submit in update mode', () => {
    mockRouter.url = '/sessions/update/1';
    component.ngOnInit();

    component.sessionForm?.setValue({
      name: 'Yoga Session Updated',
      date: '2023-12-02',
      teacher_id: 2,
      description: 'Updated description',
    });

    component.submit();
    expect(mockSessionApiService.update).toHaveBeenCalledWith('1', {
      name: 'Yoga Session Updated',
      date: '2023-12-02',
      teacher_id: 2,
      description: 'Updated description',
    });
    expect(mockSnackBar.open).toHaveBeenCalledWith(
      'Session updated !',
      'Close',
      { duration: 3000 }
    );
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  // Test pour vérifier que les professeurs sont récupérés à l'initialisation
  it('should fetch teachers on init', () => {
    expect(mockTeacherService.all).toHaveBeenCalled();
  });
});

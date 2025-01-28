import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  } 

  beforeEach(async () => {
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
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});


// import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
// import { ComponentFixture, TestBed } from '@angular/core/testing';
// import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
// import { MatCardModule } from '@angular/material/card';
// import { MatFormFieldModule } from '@angular/material/form-field';
// import { MatIconModule } from '@angular/material/icon';
// import { MatInputModule } from '@angular/material/input';
// import { MatSelectModule } from '@angular/material/select';
// import { MatSnackBarModule } from '@angular/material/snack-bar';
// import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
// import { RouterTestingModule } from '@angular/router/testing';
// import { of } from 'rxjs';
// import { expect } from '@jest/globals';
// import { SessionService } from 'src/app/services/session.service';
// import { SessionApiService } from 'src/app/features/sessions/services/session-api.service';
// import { TeacherService } from 'src/app/services/teacher.service';
// import { Session } from 'src/app/features/sessions/interfaces/session.interface';
// import { FormComponent } from './form.component';

// describe('FormComponent', () => {
//   let component: FormComponent;
//   let fixture: ComponentFixture<FormComponent>;
//   let sessionApiService: SessionApiService;
//   let teacherService: TeacherService;
//   let httpMock: HttpTestingController;
//   let fb: FormBuilder;

//   const mockSessionService = {
//     sessionInformation: {
//       admin: true,
//       id: 1
//     }
//   };

//   const mockSession: Session = {
//     id: 1,
//     name: 'Session 1',
//     description: 'Description 1',
//     date: new Date(),
//     teacher_id: 1,
//     users: [1],
//     createdAt: new Date(),
//     updatedAt: new Date()
//   };

//   beforeEach(async () => {
//     await TestBed.configureTestingModule({
//       imports: [
//         RouterTestingModule,
//         HttpClientTestingModule,
//         MatCardModule,
//         MatIconModule,
//         MatFormFieldModule,
//         MatInputModule,
//         ReactiveFormsModule,
//         MatSnackBarModule,
//         MatSelectModule,
//         NoopAnimationsModule // Utiliser NoopAnimationsModule pour désactiver les animations
//       ],
//       providers: [
//         { provide: SessionService, useValue: mockSessionService },
//         SessionApiService,
//         TeacherService,
//         FormBuilder
//       ],
//       declarations: [FormComponent]
//     }).compileComponents();

//     fixture = TestBed.createComponent(FormComponent);
//     component = fixture.componentInstance;
//     sessionApiService = TestBed.inject(SessionApiService);
//     teacherService = TestBed.inject(TeacherService);
//     httpMock = TestBed.inject(HttpTestingController);
//     fb = TestBed.inject(FormBuilder);
//     fixture.detectChanges();
//   });

//   afterEach(() => {
//     httpMock.verify(); // Vérifie qu'il n'y a pas de requêtes HTTP en attente
//   });

//   // Test 1 : Vérifier que le composant est bien créé
//   it('should create', () => {
//     expect(component).toBeTruthy();
//   });

//   // Test 2 : Vérifier que la méthode ngOnInit initialise correctement le formulaire
//   it('should initialize the form on ngOnInit', () => {
//     component.id = '1'; // Assurez-vous que l'ID est défini
//     const req = httpMock.expectOne(`api/session/${component.id}`);
//     expect(req.request.method).toBe('GET');
//     req.flush(mockSession);

//     component.ngOnInit();

//     expect(component.sessionForm).toBeDefined();
//     expect(component.sessionForm?.value).toEqual({
//       name: mockSession.name,
//       date: new Date(mockSession.date).toISOString().split('T')[0],
//       teacher_id: mockSession.teacher_id,
//       description: mockSession.description
//     });
//   });

//   // Test 3 : Vérifier que la méthode submit crée ou met à jour une session
//   it('should create or update a session on submit', () => {
//     const session = {
//       name: 'New Session',
//       date: new Date().toISOString().split('T')[0],
//       teacher_id: 1,
//       description: 'New Description'
//     };

//     component.sessionForm = fb.group({
//       name: [session.name, [Validators.required]],
//       date: [session.date, [Validators.required]],
//       teacher_id: [session.teacher_id, [Validators.required]],
//       description: [session.description, [Validators.required, Validators.max(2000)]]
//     });

//     // Simuler la réponse de l'API pour la création de la session
//     const createReq = httpMock.expectOne('api/session');
//     expect(createReq.request.method).toBe('POST');
//     createReq.flush(mockSession);

//     component.submit();

//     expect(component.matSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
//     expect(component.router.navigate).toHaveBeenCalledWith(['sessions']);
//   });

//   // Test 4 : Vérifier que la méthode exitPage affiche un message et redirige l'utilisateur
//   it('should display a message and navigate to sessions page', () => {
//     const message = 'Session created !';
//     const spySnackBar = jest.spyOn(component.matSnackBar, 'open');
//     const spyRouter = jest.spyOn(component.router, 'navigate');

//     component.exitPage(message);

//     expect(spySnackBar).toHaveBeenCalledWith(message, 'Close', { duration: 3000 });
//     expect(spyRouter).toHaveBeenCalledWith(['sessions']);
//   });
// });
package br.com.contratacaoEstagio.empresa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.com.contratacaoEstagio.contrato.ContratoRepository;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private ContratoRepository contratoRepository;

    @InjectMocks
    private EmpresaService empresaService;

    @Test
    void naoDeveExcluirEmpresaComContratosVinculados() {
        when(empresaRepository.existsById(1L)).thenReturn(true);
        when(contratoRepository.existsByEmpresa_Id(1L)).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> empresaService.excluirEmpresa(1L));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(empresaRepository, never()).deleteById(1L);
    }

    @Test
    void deveExcluirEmpresaSemContratosVinculados() {
        when(empresaRepository.existsById(1L)).thenReturn(true);
        when(contratoRepository.existsByEmpresa_Id(1L)).thenReturn(false);

        empresaService.excluirEmpresa(1L);

        verify(empresaRepository).deleteById(1L);
    }
}

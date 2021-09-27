package leilao.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.service.EnviadorDeEmails;
import br.com.alura.leilao.service.FinalizarLeilaoService;

class FinalizarLeilaoTestService
{
    private FinalizarLeilaoService service;
    @Mock
    LeilaoDao leilaoDao;
    
    @Mock
    EnviadorDeEmails enviadorDeEmails;

    @BeforeEach
    void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        service = new FinalizarLeilaoService(leilaoDao, enviadorDeEmails);
    }

    @Test
    void deveriaFinalizarUmLeilao()
    {
        //Criar uma lista com valores
        List<Leilao> leiloes = leiloes();
        
        //Ao encontrar uma lista, não utilizar o banco de dados e sim
        // a lista com valores gerada no teste
        Mockito.when(leilaoDao.buscarLeiloesExpirados())
        .thenReturn(leiloes);
        
        service.finalizarLeiloesExpirados();
        
        Leilao leilao = leiloes.get(0);
        Lance maiorLance = leilao.getLanceVencedor();
       
        //Verify já efetua o assert
        Mockito.verify(enviadorDeEmails).enviarEmailVencedorLeilao(maiorLance);
        
    }
    
    @Test
    void deveriaEnviarUmEmail()
    {
        //Criar uma lista com valores
        List<Leilao> leiloes = leiloes();
        
        //Ao encontrar uma lista, não utilizar o banco de dados e sim
        // a lista com valores gerada no teste
        Mockito.when(leilaoDao.buscarLeiloesExpirados())
        .thenReturn(leiloes);
        
        service.finalizarLeiloesExpirados();
        
        Leilao leilao = leiloes.get(0);
        Lance maiorLance = leilao.getLanceVencedor();
       
        //Verify já efetua o assert
        Mockito.verify(enviadorDeEmails).enviarEmailVencedorLeilao(maiorLance);
       
    }
    
    
    @Test
    void naoEnviarUmEmailQuandoOcorrerExcecao()
    {
        //Criar uma lista com valores
        List<Leilao> leiloes = leiloes();
        
        //Ao encontrar uma lista, não utilizar o banco de dados e sim
        // a lista com valores gerada no teste
        Mockito.when(leilaoDao.buscarLeiloesExpirados())
        .thenReturn(leiloes);
        
        Mockito.when(leilaoDao.salvar(Mockito.any()))
         .thenThrow(RuntimeException.class);
        
        try
        {
            service.finalizarLeiloesExpirados();           
            Mockito.verifyNoInteractions(enviadorDeEmails);            
        }
        catch (Exception e)
        {

        }
        
    }
    
    private List<Leilao> leiloes(){
        List<Leilao> lista = new ArrayList<>();
        
        Leilao leilao = new Leilao("Celular", new BigDecimal(500), new Usuario("Fulano"));
        
        Lance primeiro = new Lance(new Usuario("Beltrano"), new BigDecimal(600));
        Lance segundo  = new Lance(new Usuario("Ciclano"), new BigDecimal(900));
        
        leilao.propoe(primeiro);
        leilao.propoe(segundo);
        
        lista.add(leilao);
        return lista;
    }

}

package leilao.services;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.service.GeradorDePagamento;

class GeradorDePagamentoTestService
{

    private GeradorDePagamento service;
    
    @Mock
    private PagamentoDao pagamentoDao;
    
    @Mock 
    private Clock clock;
    
    //Captar um objeto utilizado
    @Captor
    private ArgumentCaptor<Pagamento> pagamento;
    
    @BeforeEach
    void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        service = new GeradorDePagamento(pagamentoDao, clock);
    }

    @Test
    void DeveriaGerarPagamentoParaVencimentosDeSegundaAQuinta()
    {
        Leilao leilao = leiloes();
        
        Lance maiorLance = leilao.getLanceVencedor();
        
        LocalDate data = LocalDate.of(2021, 9, 23);
        
        Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();
        
        Mockito.when(clock.instant()).thenReturn(instant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        
        service.gerarPagamento(maiorLance);
        
        Mockito.verify(pagamentoDao).salvar(pagamento.capture());
        
        Pagamento pagto = pagamento.getValue();
        
        Assert.assertEquals(pagto.getVencimento(), data.plusDays(1));
        Assert.assertEquals(pagto.getValor(), maiorLance.getValor());
        Assert.assertEquals(pagto.getUsuario(), maiorLance.getUsuario());
    }
    
    @Test
    void DeveriaGerarPagamentoParaVencimentosSexta()
    {
        Leilao leilao = leiloes();
        
        Lance maiorLance = leilao.getLanceVencedor();
        
        LocalDate data = LocalDate.of(2021, 9, 24);
        
        Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();
        
        Mockito.when(clock.instant()).thenReturn(instant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        
        service.gerarPagamento(maiorLance);
        
        Mockito.verify(pagamentoDao).salvar(pagamento.capture());
        
        Pagamento pagto = pagamento.getValue();
        
        Assert.assertEquals(pagto.getVencimento(), data.plusDays(3));
        Assert.assertEquals(pagto.getValor(), maiorLance.getValor());
        Assert.assertEquals(pagto.getUsuario(), maiorLance.getUsuario());
    }
    
    @Test
    void DeveriaGerarPagamentoParaVencimentosSabado()
    {
        Leilao leilao = leiloes();
        
        Lance maiorLance = leilao.getLanceVencedor();
        
        LocalDate data = LocalDate.of(2021, 9, 25);
        
        Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();
        
        Mockito.when(clock.instant()).thenReturn(instant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        
        service.gerarPagamento(maiorLance);
        
        Mockito.verify(pagamentoDao).salvar(pagamento.capture());
        
        Pagamento pagto = pagamento.getValue();
        
        Assert.assertEquals(pagto.getVencimento(), data.plusDays(2));
        Assert.assertEquals(pagto.getValor(), maiorLance.getValor());
        Assert.assertEquals(pagto.getUsuario(), maiorLance.getUsuario());
    }
    
    @Test
    void DeveriaGerarPagamentoParaVencimentosDomingo()
    {
        Leilao leilao = leiloes();
        
        Lance maiorLance = leilao.getLanceVencedor();
        
        LocalDate data = LocalDate.of(2021, 9, 26);
        
        Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();
        
        Mockito.when(clock.instant()).thenReturn(instant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        
        service.gerarPagamento(maiorLance);
        
        Mockito.verify(pagamentoDao).salvar(pagamento.capture());
        
        Pagamento pagto = pagamento.getValue();
        
        Assert.assertEquals(pagto.getVencimento(), data.plusDays(1));
        Assert.assertEquals(pagto.getValor(), maiorLance.getValor());
        Assert.assertEquals(pagto.getUsuario(), maiorLance.getUsuario());
    }
    
    private Leilao leiloes(){
        List<Leilao> lista = new ArrayList<>();
        
        Leilao leilao = new Leilao("Celular", new BigDecimal(500), new Usuario("Fulano"));
        
        Lance lance  = new Lance(new Usuario("Ciclano"), new BigDecimal(900));
        
        leilao.propoe(lance);
        leilao.setLanceVencedor(lance);
        
        return leilao;
    }


}


const overlay = document.getElementById('overlay');
const dados = document.getElementById('dados');
const localizacao = document.getElementById('localizacao');
const anoIngresso = document.getElementById('ano-ingresso');
const formAluno = document.getElementById('formAluno');
const modoEdicao = overlay.classList.contains('modo-edicao');

// Abrir modal
document.getElementById('abrir').onclick = () => {
    limparFormulario();
    formAluno.action = '/aluno';
    overlay.classList.add('ativo');
    dados.classList.add('ativo');
    localizacao.classList.remove('ativo', 'saiu');
};

// Fechar modal
function fecharModal() {
    overlay.classList.remove('ativo');
    dados.classList.remove('ativo', 'saiu');
    localizacao.classList.remove('ativo', 'saiu');
}

// Registrar o evento nos dois botões
document.querySelectorAll('.fechar').forEach(botao => {
    botao.addEventListener('click', fecharModal);
});

// Fechar clicando fora do modal
overlay.addEventListener('click', (e) => {
    if (e.target === overlay) {
        fecharModal();
    }
});

// Fechar pressionando ESC
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && overlay.classList.contains('ativo')) {
        fecharModal();
    }
});

if (modoEdicao) {
    overlay.classList.add('ativo');
    dados.classList.add('ativo');
}

// Validar os campos de uma etapa
function validar(secao) {
    let primeiroInvalido = null;

    secao.querySelectorAll('.msg-erro').forEach(m => m.remove());
    secao.querySelectorAll('.invalido').forEach(c => {
        c.classList.remove('invalido');
    });

    for (const campo of secao.querySelectorAll('input, select, textarea')) {
        if (campo.checkValidity()) continue;

        const container = campo.closest('.campo') || campo.parentElement;

        if (!container.querySelector('.msg-erro')) {
            const msg = document.createElement('small');

            msg.className = 'msg-erro';
            msg.textContent = campo.type === 'radio'
                ? 'Selecione uma opção.'
                : 'Preencha este campo.';

            container.appendChild(msg);
        }

        campo.classList.add('invalido');
        primeiroInvalido ??= campo;
    }

    primeiroInvalido?.focus();

    return !primeiroInvalido;
}

// Remover mensagens de erro ao preencher os campos
[dados, localizacao].forEach(secao => {
    secao.addEventListener('input', (e) => {
        const campo = e.target;
        const container = campo.closest('.campo') || campo.parentElement;

        container.querySelectorAll('.invalido').forEach(c => {
            c.classList.remove('invalido');
        });

        container.querySelector('.msg-erro')?.remove();
    });

    secao.addEventListener('change', (e) => {
        const campo = e.target;
        const container = campo.closest('.campo') || campo.parentElement;

        container.querySelectorAll('.invalido').forEach(c => {
            c.classList.remove('invalido');
        });

        container.querySelector('.msg-erro')?.remove();
    });
});

// Avançar para a etapa de localização
document.getElementById('continuar').onclick = () => {
    if (!validar(dados)) return;

    dados.classList.remove('ativo');
    dados.classList.add('saiu');

    localizacao.classList.add('ativo');
};

// Voltar para a etapa de dados
document.getElementById('voltar').onclick = () => {
    localizacao.classList.remove('ativo');
    dados.classList.remove('saiu');
    dados.classList.add('ativo');
};

// Validar antes de enviar o formulário
formAluno.addEventListener('submit', (e) => {
    if (!validar(localizacao)) {
        e.preventDefault();
    }
});

// Limitar ano de ingresso ao ano atual
anoIngresso.max = new Date().getFullYear();

anoIngresso.addEventListener('keydown', (e) => {
    if (['-', '+', 'e', 'E', '.', ','].includes(e.key)) {
        e.preventDefault();
    }
});

anoIngresso.addEventListener('input', () => {
    if (anoIngresso.value.length > 4) {
        anoIngresso.value = anoIngresso.value.slice(0, 4);
    }
});

anoIngresso.addEventListener('blur', () => {
    if (anoIngresso.value === '') return;

    const min = Number(anoIngresso.min);
    const max = Number(anoIngresso.max);
    const valor = Number(anoIngresso.value);

    if (valor < min) {
        anoIngresso.value = min;
    } else if (valor > max) {
        anoIngresso.value = max;
    }
});

// Consulta ao ViaCEP
const cep = document.getElementById('cep');

cep.addEventListener('input', () => {
    let v = cep.value.replace(/\D/g, '').slice(0, 8);

    if (v.length > 5) {
        v = v.slice(0, 5) + '-' + v.slice(5);
    }

    cep.value = v;
});

cep.addEventListener('blur', async () => {
    const digitos = cep.value.replace(/\D/g, '');

    if (digitos.length !== 8) return;

    try {
        const resposta = await fetch(
            `https://viacep.com.br/ws/${digitos}/json/`
        );

        const endereco = await resposta.json();

        if (endereco.erro) return;

        document.getElementById('endereco').value =
            endereco.logradouro || '';

        document.getElementById('bairro').value =
            endereco.bairro || '';

        document.getElementById('cidade').value =
            endereco.localidade || '';

        document.getElementById('numero').focus();

    } catch (erro) {
        console.error('Erro ao consultar o ViaCEP:', erro);
    }
});

// Limpar os campos do formulário
function limparFormulario() {
    [dados, localizacao].forEach(secao => {
        secao.querySelectorAll('input, select, textarea').forEach(campo => {
            if (campo.type === 'radio' || campo.type === 'checkbox') {
                campo.checked = false;
            } else if (campo.tagName === 'SELECT') {
                campo.selectedIndex = 0;
            } else {
                campo.value = '';
            }
        });

        secao.querySelectorAll('.msg-erro').forEach(m => m.remove());

        secao.querySelectorAll('.invalido').forEach(c => {
            c.classList.remove('invalido');
        });
    });
}

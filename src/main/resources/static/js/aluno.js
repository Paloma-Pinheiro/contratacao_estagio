const overlay = document.getElementById('overlay');
const dados = document.getElementById('dados');
const localizacao = document.getElementById('localizacao');
const anoIngresso = document.getElementById('ano-ingresso');

document.getElementById('abrir').onclick = () => {
    overlay.classList.add('ativo');
    dados.classList.add('ativo');
};

document.getElementById('fechar').onclick = () => {
    overlay.classList.remove('ativo');
    dados.classList.remove('ativo', 'saiu');
    localizacao.classList.remove('ativo', 'saiu');
};

overlay.addEventListener('click', (e) => {
    if (e.target === overlay) document.querySelector('.fechar').click();
});

document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && overlay.classList.contains('ativo')) {
        document.querySelector('.fechar').click();
    }
});

document.getElementById('continuar').onclick = () => {
    const campos = dados.querySelectorAll('input, select, textarea');
    let primeiroInvalido = null;

    dados.querySelectorAll('.msg-erro').forEach(m => m.remove());
    dados.querySelectorAll('.invalido').forEach(c => c.classList.remove('invalido'));

    for (const campo of campos) {
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

    if (primeiroInvalido) {
        primeiroInvalido.focus();
        return;
    }

    dados.classList.remove('ativo');
    dados.classList.add('saiu');
    localizacao.classList.add('ativo');
};
dados.addEventListener('input', (e) => {
    const campo = e.target;
    campo.classList.remove('invalido');
    campo.closest('.campo')?.querySelector('.msg-erro')?.remove();
});

document.getElementById('voltar').onclick = () => {
    localizacao.classList.remove('ativo');
    dados.classList.remove('saiu');
    dados.classList.add('ativo');
};

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

    if (valor < min) anoIngresso.value = min;
    else if (valor > max) anoIngresso.value = max;
});

function validar(secao) {
    let primeiroInvalido = null;

    secao.querySelectorAll('.msg-erro').forEach(m => m.remove());
    secao.querySelectorAll('.invalido').forEach(c => c.classList.remove('invalido'));

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

[dados, localizacao].forEach(secao => {
    secao.addEventListener('input', (e) => {
        const container = e.target.closest('.campo') || e.target.parentElement;
        container.querySelectorAll('.invalido').forEach(c => c.classList.remove('invalido'));
        container.querySelector('.msg-erro')?.remove();
    });
});

document.getElementById('continuar').onclick = () => {
    if (!validar(dados)) return;

    dados.classList.remove('ativo');
    dados.classList.add('saiu');
    localizacao.classList.add('ativo');
};

document.getElementById('finalizar').onclick = () => {
    if (!validar(localizacao)) return;

    const endereco = {
        aluno_ra: document.getElementById('ra').value, // id do input de RA da etapa 1
        cep: document.getElementById('cep').value.replace(/\D/g, ''),
        endereco: document.getElementById('endereco').value.trim(),
        numero: document.getElementById('numero').value.trim(),
        complemento: document.getElementById('complemento').value.trim(),
        bairro: document.getElementById('bairro').value.trim(),
        cidade: document.getElementById('cidade').value.trim()
    };

    console.log(endereco);
};

// API CEP
const cep = document.getElementById('cep');

cep.addEventListener('input', () => {
    let v = cep.value.replace(/\D/g, '').slice(0, 8);
    if (v.length > 5) v = v.slice(0, 5) + '-' + v.slice(5);
    cep.value = v;
});

cep.addEventListener('blur', async () => {
    const digitos = cep.value.replace(/\D/g, '');
    if (digitos.length !== 8) return;

    try {
        const r = await fetch(`https://viacep.com.br/ws/${digitos}/json/`);
        const d = await r.json();
        if (d.erro) return;

        document.getElementById('endereco').value = d.logradouro || '';
        document.getElementById('bairro').value = d.bairro || '';
        document.getElementById('cidade').value = d.localidade || '';
        document.getElementById('numero').focus();
    } catch {
    }
});

document.getElementById('finalizar').onclick = async () => {
    if (!validar(localizacao)) return;

    const valor = (id) => document.getElementById(id).value.trim();

    const payload = {
        ra: valor('ra'),
        nome: valor('nome'),
        anoIngresso: Number(valor('ano-ingresso')),
        curso: valor('curso'),
        etnia: valor('etnia'),
        faixaEtaria: valor('faixa-etaria'),
        genero: valor('genero'),
        modalidadeIngresso: valor('modalidade-ingresso'),
        pcd: document.querySelector('input[name="pcd"]:checked').value === 'sim',
        tentativas: Number(valor('tentativas')),
        rendaFamiliar: valor('renda-familiar'),
        semestreAtual: Number(valor('semestre-atual')),
        endereco: {
            cep: valor('cep').replace(/\D/g, ''),
            endereco: valor('endereco'),
            numero: valor('numero'),
            complemento: valor('complemento'),
            bairro: valor('bairro'),
            cidade: valor('cidade')
        }
    };

    try {
        const resp = await fetch('/api/aluno', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (resp.status === 201) {
            document.querySelector('.fechar').click();
            location.reload(); 
        } else if (resp.status === 409) {
            alert('Já existe um aluno com esse RA.');
        } else {
            alert('Verifique os dados e tente novamente.');
        }
    } catch {
        alert('Erro de conexão com o servidor.');
    }
};

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
        secao.querySelectorAll('.invalido').forEach(c => c.classList.remove('invalido'));
    });
}
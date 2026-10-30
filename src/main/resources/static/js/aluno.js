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
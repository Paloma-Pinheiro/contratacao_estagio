const overlay = document.getElementById('overlay');
const dados = document.getElementById('dados');
const formEmpresa = document.getElementById('formEmpresa');
const tituloFormulario = formEmpresa.querySelector('.titulo-cadastro');
const modoEdicao = overlay.classList.contains('modo-edicao');

function abrirModal() {
    overlay.classList.add('ativo');
    dados.classList.add('ativo');
}

function fecharModal() {
    overlay.classList.remove('ativo');
    dados.classList.remove('ativo');
}

document.getElementById('abrir').addEventListener('click', () => {
    formEmpresa.querySelectorAll('input:not([type="hidden"])').forEach(campo => {
        campo.value = '';
    });
    formEmpresa.action = '/empresa';
    tituloFormulario.textContent = 'Cadastro da Empresa';
    overlay.classList.remove('modo-edicao');
    formEmpresa.querySelectorAll('.msg-erro').forEach(mensagem => mensagem.remove());
    formEmpresa.querySelectorAll('.invalido').forEach(campo => {
        campo.classList.remove('invalido');
    });
    abrirModal();
});

document.getElementById('fechar').addEventListener('click', fecharModal);

overlay.addEventListener('click', (evento) => {
    if (evento.target === overlay) fecharModal();
});

document.addEventListener('keydown', (evento) => {
    if (evento.key === 'Escape' && overlay.classList.contains('ativo')) {
        fecharModal();
    }
});

formEmpresa.addEventListener('input', (evento) => {
    const campo = evento.target;
    campo.classList.remove('invalido');
    campo.closest('.campo')?.querySelector('.msg-erro')?.remove();
});

function validarFormulario() {
    let primeiroInvalido = null;
    const campos = formEmpresa.querySelectorAll('input:not([type="hidden"])');

    formEmpresa.querySelectorAll('.msg-erro').forEach(mensagem => mensagem.remove());
    formEmpresa.querySelectorAll('.invalido').forEach(campo => {
        campo.classList.remove('invalido');
    });

    for (const campo of campos) {
        campo.value = campo.value.trim();
        if (campo.checkValidity()) continue;

        const container = campo.closest('.campo');
        const mensagem = document.createElement('small');
        mensagem.className = 'msg-erro';
        mensagem.textContent = 'Preencha este campo.';
        container.appendChild(mensagem);

        campo.classList.add('invalido');
        primeiroInvalido ??= campo;
    }

    primeiroInvalido?.focus();
    return !primeiroInvalido;
}

formEmpresa.addEventListener('submit', (evento) => {
    if (!validarFormulario()) evento.preventDefault();
});

if (modoEdicao) abrirModal();

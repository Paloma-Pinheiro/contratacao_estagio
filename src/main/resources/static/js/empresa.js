const overlay = document.getElementById('overlay');
const dados = document.getElementById('dados');

document.getElementById('abrir').onclick = () => {
    overlay.classList.add('ativo');
    dados.classList.add('ativo');
};

document.getElementById('fechar').onclick = () => {
    overlay.classList.remove('ativo');
    dados.classList.remove('ativo');
};

overlay.addEventListener('click', (e) => {
    if (e.target === overlay) document.getElementById('fechar').click();
});

document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && overlay.classList.contains('ativo')) {
        document.getElementById('fechar').click();
    }
});

dados.addEventListener('input', (e) => {
    const campo = e.target;
    campo.classList.remove('invalido');
    campo.closest('.campo')?.querySelector('.msg-erro')?.remove();
});

function validarFormulario() {
    let primeiroInvalido = null;
    const campos = dados.querySelectorAll('input');

    dados.querySelectorAll('.msg-erro').forEach(m => m.remove());
    dados.querySelectorAll('.invalido').forEach(c => c.classList.remove('invalido'));

    for (const campo of campos) {
        if (campo.checkValidity()) continue;

        const container = campo.closest('.campo');
        if (!container.querySelector('.msg-erro')) {
            const msg = document.createElement('small');
            msg.className = 'msg-erro';
            msg.textContent = 'Preencha este campo.';
            container.appendChild(msg);
        }

        campo.classList.add('invalido');
        primeiroInvalido ??= campo;
    }

    if (primeiroInvalido) {
        primeiroInvalido.focus();
        return false;
    }
    return true;
}
document.getElementById('finalizar').onclick = async () => {
    if (!validarFormulario()) return;

    const razaoSocial = document.getElementById('razaoSocial').value.trim();
    const segmento = document.getElementById('segmento').value.trim();
    const areaAtuacao = document.getElementById('areaAtuacao').value.trim();

    const payload = {
        razaoSocial: razaoSocial,
        segmento: segmento,
        areaAtuacao: areaAtuacao,
        ativo: true // Campo padrão da entidade
    };

    try {
        const resp = await fetch('/api/empresa', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (resp.status === 201) {
            document.getElementById('fechar').click();
            location.reload(); 
        } else {
            alert('Verifique os dados e tente novamente.');
        }
    } catch {
        alert('Erro de conexão com o servidor.');
    }
};
async function excluirEmpresa(id) {
    if (!confirm('Deseja realmente excluir esta empresa?')) return;

    try {
        const resp = await fetch(`/api/empresa/${id}`, { method: 'DELETE' });
        if (resp.ok || resp.status === 204) {
            location.reload();
        } else {
            alert('Erro ao excluir a empresa.');
        }
    } catch {
        alert('Erro de conexão com o servidor.');
    }
}

async function alternarStatus(id) {
    try {
        const resp = await fetch(`/api/empresa/${id}/ativar`, { method: 'POST' });
        if (resp.ok) {
            location.reload();
        } else {
            alert('Erro ao alterar o status da empresa.');
        }
    } catch {
        alert('Erro de conexão com o servidor.');
    }
}
const overlay = document.getElementById("overlay");
const form = document.getElementById("formContrato");
const tituloModal = document.getElementById("tituloModal");
const contratoId = document.getElementById("contratoId");
const mensagemErro = document.getElementById("mensagemErro");
const botaoSalvar = document.getElementById("salvar");

const dataInicio = document.getElementById("dataInicio");
const dataTermino = document.getElementById("dataTermino");
const ativo = document.getElementById("ativo");

// ABRIR MODAL PARA CADASTRAR
document.getElementById("abrir").addEventListener("click", () => {
    form.querySelectorAll("input:not([type='hidden']), select, textarea")
        .forEach((campo) => {
            if (campo.tagName === "SELECT") {
                campo.selectedIndex = 0;
            } else {
                campo.value = "";
            }
        });

    form.action = form.dataset.createAction;
    dataTermino.min = "";
    contratoId.value = "";
    ativo.value = "true";
    tituloModal.textContent = "Cadastrar contrato";
    botaoSalvar.textContent = "Salvar contrato";
    limparMensagem();
    overlay.classList.add("ativo");
});

// FECHAR MODAL
function fecharModal() {
    overlay.classList.remove("ativo");
    form.reset();
    dataTermino.min = "";
    contratoId.value = "";
    limparMensagem();
}

document.getElementById("fechar").addEventListener("click", fecharModal);
document.getElementById("cancelar").addEventListener("click", fecharModal);

// FECHAR AO CLICAR FORA DO MODAL
overlay.addEventListener("click", (event) => {
    if (event.target === overlay) {
        fecharModal();
    }
});

// FECHAR COM ESC
document.addEventListener("keydown", (event) => {
    if (event.key === "Escape" && overlay.classList.contains("ativo")) {
        fecharModal();
    }
});

function limparMensagem() {
    mensagemErro.textContent = "";
    dataTermino.classList.remove("invalido");
}

// VALIDAR DATAS
dataInicio.addEventListener("change", () => {
    dataTermino.min = dataInicio.value;

    if (dataTermino.value && dataTermino.value < dataInicio.value) {
        dataTermino.value = "";
    }
});

if (dataInicio.value) {
    dataTermino.min = dataInicio.value;
}

form.addEventListener("submit", (event) => {
    limparMensagem();

    if (dataInicio.value && dataTermino.value
            && dataTermino.value < dataInicio.value) {
        event.preventDefault();
        mensagemErro.textContent =
            "A data de término não pode ser anterior à data de início.";
        dataTermino.classList.add("invalido");
    }
});

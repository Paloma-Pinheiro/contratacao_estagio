
const overlay = document.getElementById("overlay");
const modal = document.getElementById("modal");
const form = document.getElementById("formContrato");

const tituloModal = document.getElementById("tituloModal");
const contratoId = document.getElementById("contratoId");
const mensagemErro = document.getElementById("mensagemErro");

const aluno = document.getElementById("aluno");
const empresa = document.getElementById("empresa");
const area = document.getElementById("area");
const cargo = document.getElementById("cargo");
const modalidade = document.getElementById("modalidade");
const horario = document.getElementById("horario");
const cargaHoraria = document.getElementById("cargaHoraria");
const dataInicio = document.getElementById("dataInicio");
const dataTermino = document.getElementById("dataTermino");
const ativo = document.getElementById("ativo");

const botaoSalvar = document.getElementById("salvar");

// ABRIR MODAL PARA CADASTRAR
document.getElementById("abrir").addEventListener("click", () => {
    form.reset();

    dataTermino.min = "";

    contratoId.value = "";
    tituloModal.textContent = "Cadastrar contrato";
    botaoSalvar.textContent = "Salvar contrato";

    limparMensagem();

    overlay.classList.add("ativo");
});

// FECHAR MODAL
function fecharModal() {
    overlay.classList.remove("ativo");
    form.reset();
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

// LIMPAR MENSAGEM DE ERRO
function limparMensagem() {
    mensagemErro.textContent = "";

    form.querySelectorAll(".invalido").forEach((campo) => {
        campo.classList.remove("invalido");
    });
}

// VALIDAR DATAS
dataInicio.addEventListener("change", () => {
    dataTermino.min = dataInicio.value;

    if (dataTermino.value && dataTermino.value < dataInicio.value) {
        dataTermino.value = "";
    }
});

// EDITAR CONTRATO
function editarContrato(botao) {

    const dados = botao.dataset;

    form.reset();
    limparMensagem();

    contratoId.value = dados.id;
    aluno.value = dados.alunoRa;
    empresa.value = dados.empresaId;
    area.value = dados.area;
    cargo.value = dados.cargo;
    modalidade.value = dados.modalidade;
    horario.value = dados.horario;
    cargaHoraria.value = dados.cargaHoraria;
    dataInicio.value = dados.dataInicio;
    dataTermino.value = dados.dataTermino;
    ativo.value = dados.ativo;

    dataTermino.min = dados.dataInicio;

    tituloModal.textContent = "Editar contrato";
    botaoSalvar.textContent = "Atualizar contrato";

    overlay.classList.add("ativo");
}

// CADASTRAR OU ATUALIZAR
form.addEventListener("submit", async (event) => {

    event.preventDefault();
    limparMensagem();

    // VERIFICAR SE A DATA DE TÉRMINO É VÁLIDA
    if (dataTermino.value < dataInicio.value) {
        mensagemErro.textContent =
            "A data de término não pode ser anterior à data de início.";

        dataTermino.classList.add("invalido");
        return;
    }

    // MONTAR OBJETO DO CONTRATO
    const contrato = {
        aluno: {
            ra: aluno.value
        },

        empresa: {
            id: Number(empresa.value)
        },

        area: area.value,
        cargo: cargo.value,
        modalidade: modalidade.value,
        horario: horario.value,
        cargaHoraria: Number(cargaHoraria.value),
        dataInicio: dataInicio.value,
        dataTermino: dataTermino.value,
        ativo: ativo.value === "true"
    };

    const id = contratoId.value;

    const url = id
        ? `/contrato/${id}`
        : "/contrato";

    const metodo = id ? "PUT" : "POST";

    botaoSalvar.disabled = true;
    botaoSalvar.textContent = "Salvando...";

    try {

        const resposta = await fetch(url, {
            method: metodo,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(contrato)
        });

        if (!resposta.ok) {
            throw new Error(await obterMensagemErro(resposta));
        }

        alert(
            id
                ? "Contrato atualizado com sucesso!"
                : "Contrato cadastrado com sucesso!"
        );

        window.location.reload();

    } catch (erro) {

        mensagemErro.textContent = erro.message;

        botaoSalvar.disabled = false;
        botaoSalvar.textContent = id
            ? "Atualizar contrato"
            : "Salvar contrato";
    }
});

// EXCLUIR CONTRATO
async function excluirContrato(botao) {

    const id = botao.dataset.id;

    if (!confirm("Tem certeza que deseja excluir este contrato?")) {
        return;
    }

    botao.disabled = true;

    try {

        const resposta = await fetch(`/contrato/${id}`, {
            method: "DELETE"
        });

        if (!resposta.ok) {
            throw new Error(await obterMensagemErro(resposta));
        }

        alert("Contrato excluído com sucesso!");

        window.location.reload();

    } catch (erro) {

        alert(erro.message);
        botao.disabled = false;
    }
}

// OBTER MENSAGEM DE ERRO DA API
async function obterMensagemErro(resposta) {

    const texto = await resposta.text();

    if (!texto) {
        return `Não foi possível realizar a operação. Status: ${resposta.status}`;
    }

    try {
        const dados = JSON.parse(texto);

        return dados.detail || dados.message || dados.title || texto;

    } catch {
        return texto;
    }
}

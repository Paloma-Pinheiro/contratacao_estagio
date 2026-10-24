
async function excluirAluno(event, formulario) {
    event.preventDefault();

    if (!confirm("Tem certeza que deseja excluir este aluno?")) {
        return;
    }

    const botao = formulario.querySelector('button[type="submit"]');
    botao.disabled = true;

    try {
        const resposta = await fetch(formulario.action, {
            method: "POST",
            body: new URLSearchParams(new FormData(formulario))
        });

        if (!resposta.ok) {
            throw new Error(
                "Não foi possível excluir o aluno. Status: " + resposta.status
            );
        }

        window.location.reload();
    } catch (erro) {
        alert(erro.message);
        botao.disabled = false;
    }
}
let tempoRestante = 90; // 1 minuto e 30 segundos
let tentativas = 0;
const MAX_TENTATIVAS = 3;

const inputs = document.querySelectorAll(".formulario-container input");
const contadorElemento = document.getElementById("contador");
const labelCodigo = document.getElementById("label-codigo");

let intervalo;

function atualizarContador() {
    const minutos = Math.floor(tempoRestante / 60);
    const segundos = tempoRestante % 60;

    contadorElemento.textContent = `Contador: ${minutos}:${segundos < 10 ? "0" : ""}${segundos}`;

    if (tempoRestante <= 0) {
        clearInterval(intervalo);
        contadorElemento.textContent = "Tempo esgotado!";
        bloquearCampos();
    } else {
        tempoRestante--;
    }
}

function bloquearCampos() {
    inputs.forEach(input => {
        input.disabled = true;
        input.value = "";
    });
}

function limparCampos() {
    inputs.forEach(input => {
        input.value = "";
    });
    inputs[0].focus();
}

async function confirmar() {
    let codigo = "";
    inputs.forEach(input => {
        codigo += input.value;
    });

    if (codigo.length !== 4 || isNaN(codigo)) {
        labelCodigo.textContent = "Código inválido.";
        limparCampos();
        return;
    }

    if (tentativas >= MAX_TENTATIVAS || tempoRestante <= 0) {
        labelCodigo.textContent = "Tentativas esgotadas.";
        bloquearCampos();
        contadorElemento.textContent = "3 tentativas incorretas.";
        clearInterval(intervalo);
        return;
    }

    try {
        const url = `http://localhost:8080/v1/verificacao/verificar-codigo?codigo=${codigo}`;
        const response = await fetch(url);

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Erro ao verificar o código.");
        }

        const data = await response.json();
        console.log("Token recebido:", data.token);
        labelCodigo.textContent = "Código correto!";
        bloquearCampos();
        clearInterval(intervalo);

    } catch (error) {
        tentativas++;
        if (error.message === "Failed to fetch") {
            labelCodigo.textContent = "Erro de conexão com o servidor.";
        } else {
            labelCodigo.textContent = error.message; // Mostra a mensagem real do backend
        }

        if (tentativas >= MAX_TENTATIVAS) {
            labelCodigo.textContent = "Tentativas esgotadas.";
            contadorElemento.textContent = "3 tentativas incorretas.";
            bloquearCampos();
            clearInterval(intervalo);
        } else {
            limparCampos();
        }
    }
}

// Autopular entre campos
inputs.forEach((input, index) => {
    input.addEventListener("input", () => {
        if (input.value.length === 1 && index < inputs.length - 1) {
            inputs[index + 1].focus();
        }
    });

    input.addEventListener("keydown", (e) => {
        if (e.key === "Backspace" && input.value === "" && index > 0) {
            inputs[index - 1].focus();
        }
    });
});

// Inicia o contador
atualizarContador();
intervalo = setInterval(atualizarContador, 1000);
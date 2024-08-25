let copyList  = document.querySelectorAll( '.copy-clipboard' );
let copyArray = Array.prototype.slice.call( copyList );

function tooltipUpdate( button, tooltip, title ) {
    tooltip.dispose();
    button.setAttribute( 'title', title );
    tooltip = new bootstrap.Tooltip( button );
    tooltip.show();

    return tooltip;
}

const unsecuredCopyToClipboard = (text) => { const textArea = document.createElement("textarea"); textArea.value=text; document.body.appendChild(textArea); textArea.focus();textArea.select(); try{document.execCommand('copy')}catch(err){console.error('Unable to copy to clipboard',err)}document.body.removeChild(textArea)};

copyArray.map( function ( copy ) {
    let text    = copy.querySelector( 'span' ).innerText;
    let button  = copy.querySelector( 'i' );
    let tooltip = new bootstrap.Tooltip( button );

    button.addEventListener( 'mouseover', function () {
        tooltip = tooltipUpdate( button, tooltip, 'Скопировать' );
        this.classList.remove( 'bi-clipboard-check' );
        this.classList.add( 'bi-clipboard-plus' );
    } );

    button.addEventListener( 'click', function () {
        let name_bot = "tkk-bot";
        let pub_key_for_bot = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAACAQCf5l3ZWJCWZsaZIWDm2" +
            "ajv1oxlKevRDE3f0M1PLmPcy/gCnMLxQgX1mbMTh8XOt9M1ql9YrwVJU+EPxfOcUNkceGeyEVV7NWN6e85" +
            "q4UwVCDE9ThPWTOqz8tkWYIqVqWwzp/cA+9qzwrGNq7UZrRiJ85uK1+wdKwwJmycXpJMdDvg1VDwIWNxXo" +
            "YKiPTm6R/2+hVrvkDJD70GgAyDbKQX2rqKLs6aSlWVChfIYJB+mEgC5MytYWlEiCiEavyYWWryPiicfzZC" +
            "anziGdbS1mJZU8Aq3c5Z8JR12dlZkB6DBT8nbWjkPWdE7gvznJQjYct4KTrPYcrUbPN0uSfkQGDGtUz8FH" +
            "lZobHruJy4SQp8CvgRrq+yyZPycSui8uVMZCpeAgG7H/Iw+MmeIZaKpGV+X7H0SUY3zXtXGudNyj6Vj15o" +
            "6fpZGtAdovaHFP3dpYNfPfhbrvww21NP9DoRtWkG6BQpFTW8f1rYSSHuuBf6WjTYoq+BiHt8hIThqUCAur" +
            "XLzJpoY2OWlT5dF/+TiDTEY8we1VMn5nJ5gVlwa25uLGX1t8Sj9W50aMWrhvP3YPXJ4sOO0lfw2Va8Chs/" +
            "o0NBOgptnsXhspHJSBfCpQ+yHICQfy7yIivVFnd0crEtDcm6TI7yMaYeaagYXRPLu1R58wWfDWEadjTx1n" +
            "AZaIpfIeQ== yoasakura13@gmail.com";

        unsecuredCopyToClipboard("new_user=" + name_bot + " &&\n" +
            "sudo adduser \"$new_user\" --disabled-password --gecos \"\" &>/dev/null &&\n" +
            "sudo usermod -aG sudo \"$new_user\" &&\n" +
            "sudo mkdir /home/\"$new_user\"/.ssh &&\n" +
            "sudo chown -R \"$new_user\":\"$new_user\" /home/\"$new_user\"/.ssh/ &&\n" +
            "sudo chmod 777 /home/\"$new_user\"/.ssh &&\n" +
            "sudo touch /home/\"$new_user\"/.ssh/authorized_keys &&\n" +
            "sudo chown -R \"$new_user\":\"$new_user\" /home/\"$new_user\"/.ssh/authorized_keys &&\n" +
            "sudo chmod 777 /home/\"$new_user\"/.ssh/authorized_keys &&\n" +
            "if sudo echo \"" + pub_key_for_bot + "\" > /home/\"$new_user\"/.ssh/authorized_keys; then >&1 echo \"SUCCESS\"; else  >&1 echo \"ERROR\"; fi &&\n" +
            "sudo chmod 600 /home/\"$new_user\"/.ssh/authorized_keys &&\n" +
            "sudo chmod 700 /home/\"$new_user\"/.ssh");
        tooltip = tooltipUpdate( button, tooltip, 'Вставь скрипт в консоль сервера и нажми Enter!' );
        this.classList.remove( 'bi-clipboard-plus' );
        this.classList.add( 'bi-clipboard-check' );
    } );
} );